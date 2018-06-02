package com.ab.worldcup.bet;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.config.ApplicationConfig;
import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.knockout.KnockoutMatchQualifier;
import com.ab.worldcup.match.Match;
import com.ab.worldcup.match.MatchService;
import com.ab.worldcup.match.Stage;
import com.ab.worldcup.results.CalculatedUserBet;
import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.results.Qualifier;
import com.ab.worldcup.results.ResultsService;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.team.Team;
import com.ab.worldcup.web.model.BetOverviewData;
import com.ab.worldcup.web.model.MatchesData;
import com.ab.worldcup.web.model.UserBetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BetService {

    @Autowired
    private BetRepository betRepository;

    @Autowired
    private UserBetRepository userBetRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private ResultsService resultsService;

    @Autowired
    private ApplicationConfig applicationConfig;


    @Cacheable("allBets")
    public List<Bet> getAllBets() {
        return betRepository.findAll();
    }

    public Bet getBetById(Long id) {
        return betRepository.findOne(id);
    }

    @SuppressWarnings("unchecked")
    public MatchesData getMatchesData(Long accountId) {
        // all user bets
        List<UserBet> allBetsForUser = userBetRepository.findByUserBetIdAccountId(accountId);
        return matchService.getMatchesData(allBetsForUser);
    }

    public void deleteUserBet(Long accountId, Long betId) {
        UserBet userBer = userBetRepository.findByUserBetIdAccountIdAndUserBetIdBetId(accountId, betId);
        if (userBer == null) {
            throw new IllegalArgumentException("User bet not found");
        }
        userBetRepository.delete(userBer);
    }

    public List<UserBet> findByUserBetIdAccountId(Long accountId) {
        return userBetRepository.findByUserBetIdAccountId(accountId);
    }

    public List<UserBet> getUserBetForGroup(Group group, Long accountId) {
        return userBetRepository.findUserBetByGroup(group.toString(), accountId);
    }

    public UserBet updateMatchBet(Account account, UserBetData userBetData) {
        Bet bet = getBetById(userBetData.getBetId());
        Match match = matchService.getMatchById(bet.getMatchId());
        UserBet userBet = userBetRepository.findByUserBetIdAccountIdAndUserBetIdBetId(account.getId(), userBetData.getBetId());
        if (userBet == null) {
            userBet = new UserBet(new UserBetId(account, bet));
        }
        // set goals
        userBet.setHomeTeamGoals(userBetData.getHomeTeamGoals());
        userBet.setAwayTeamGoals(userBetData.getAwayTeamGoals());
        // knockout match
        if (!Stage.GROUP.equals(match.getStageId())) {
            userBet.setHomeTeam(groupService.findTeamByCode(userBetData.getHomeTeamCode()));
            userBet.setAwayTeam(groupService.findTeamByCode(userBetData.getAwayTeamCode()));
            if (KnockoutMatchQualifier.HOME_TEAM.equals(userBetData.getMatchQualifier())) {
                userBet.setQualifier(userBet.getHomeTeam());
            } else {
                userBet.setQualifier(userBet.getAwayTeam());
            }
        } else {
            userBet.setHomeTeam(match.getHomeTeam());
            userBet.setAwayTeam(match.getAwayTeam());
        }
        return userBetRepository.save(userBet);
    }

    public List<BetOverviewData> getOverview(Long accountId) {
        List<BetOverviewData> betOverviewData = new ArrayList<>();

        List<Bet> bets = betRepository.findAll();
        List<UserBet> betForAccount = findByUserBetIdAccountId(accountId);

        // matches map by match id
        Map<Long, ? extends Match> matchesMap = matchService.getAllMatches().stream()
                .collect(Collectors.toMap(Match::getMatchId, Function.identity()));
        // match results by match id
        Map<Long, MatchResult> resultMap = resultsService.getAllMatchResults().stream()
                .collect(Collectors.toMap(MatchResult::getMatchId, Function.identity()));
        // calculated results by bet id
        Map<Long, CalculatedUserBet> betMap = resultsService.calculateBetsForUser(betForAccount).stream()
                .collect(Collectors.toMap(b -> b.getUserBet().getUserBetId().getBet().getId(), Function.identity()));

        bets.forEach(bet -> {
            if (bet.getType().equals(BetType.MATCH)) {
                betOverviewData.add(BetOverviewData.builder()
                        .bet(bet)
                        .match(matchesMap.get(bet.getMatchId()))
                        .matchResult(resultMap.get(bet.getMatchId()))
                        .calculatedUserBet(betMap.get(bet.getId())).build());
            } else if (bet.getType().equals(BetType.QUALIFIER)) {
                betOverviewData.add(BetOverviewData.builder()
                        .bet(bet)
                        .calculatedUserBet(betMap.get(bet.getId())).build());
            }
        });

        betOverviewData.sort((o1, o2) -> (int) (o1.getBet().getId() - o2.getBet().getId()));
        return betOverviewData;
    }


    public void setQualifiersBets(Account account, Map<Stage, List<Qualifier>> userQualifiersByStageMap) {
        List<Bet> qualifierBets = betRepository.findAllByType(BetType.QUALIFIER);
        Map<Stage, List<Bet>> stageListMap = qualifierBets.stream().collect(Collectors.groupingBy(Bet::getStageId));

        List<UserBet> betForAccount = findByUserBetIdAccountId(account.getId());

        // user bets by match id
        Map<Long, UserBet> userBetMap = betForAccount.stream().filter(b -> b.getUserBetId().getBet().getType().equals(BetType.QUALIFIER))
                .collect(Collectors.toMap(b -> b.getUserBetId().getBet().getId(), Function.identity()));

        userQualifiersByStageMap.forEach((stage, userQualifierList) -> {
            List<Bet> qualifierBetsByStage = stageListMap.get(stage);
            if (qualifierBetsByStage != null) {
                int qualifierBetsByStageSize = qualifierBetsByStage.size();
                int qualifierIndex = 0;
                for (Qualifier userQualifier : userQualifierList) {
                    if (qualifierBetsByStageSize > qualifierIndex) {
                        Bet bet = qualifierBetsByStage.get(qualifierIndex++);
                        UserBet userBet = userBetMap.get(bet.getId());
                        if (userBet == null) {
                            userBet = new UserBet(new UserBetId(account, bet));
                        }
                        userBet.setQualifier(userQualifier.getTeam());
                        userBet.setKnockoutTeamCode(userQualifier.getKnockoutTeamCode());
                        userBetRepository.save(userBet);
                    }
                }
            }
        });
    }

    public QualifierBetData getQualifierBets(Long accountId) {
        List<UserBet> allBetsForUser = userBetRepository.findByUserBetIdAccountId(accountId);
        List<Qualifier> qualifierList = allBetsForUser.stream().
                filter(t -> t.getUserBetId().getBet().getType().equals(BetType.QUALIFIER)).
                map(t -> buildQualifier(t.getUserBetId().getBet().getStageId(), t.getQualifier(), t.getKnockoutTeamCode())).
                collect(Collectors.toList());

        return QualifierBetData.builder()
                .qualifiersList(qualifierList)
                .lockTime(Timestamp.valueOf(applicationConfig.getStartDateTime()))
                .build();
    }


    private Qualifier buildQualifier(Stage stage, Team team, KnockoutTeamCode teamCode) {
        return Qualifier.builder().stageId(stage).team(team).knockoutTeamCode(teamCode).build();
    }

    public BetStatisticsData getBetStats(Long betId) {
        List<UserBet> allUserBetsByBetId = userBetRepository.findByUserBetIdBetId(betId);
        int betsCount = allUserBetsByBetId.size();
        int drawBets = 0;
        int homeTeamBets = 0;
        int awayTeamBets = 0;

        if (betsCount > 0) {
            homeTeamBets = (int) allUserBetsByBetId.stream().filter(b -> b.getHomeTeamGoals() > b.getAwayTeamGoals()).count() * 100 / betsCount;
            awayTeamBets = (int) allUserBetsByBetId.stream().filter(b -> b.getHomeTeamGoals() < b.getAwayTeamGoals()).count() * 100 / betsCount;
            drawBets = (int) allUserBetsByBetId.stream().filter(b -> b.getHomeTeamGoals().equals(b.getAwayTeamGoals())).count() * 100 / betsCount;
        }

        return BetStatisticsData.builder().
                betsOnHomeTeamPercent(homeTeamBets).
                betsOnAwayTeamPercent(awayTeamBets).
                betsOnDrawPercent(drawBets).
                build();
    }
}
