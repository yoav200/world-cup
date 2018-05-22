package com.ab.worldcup.bet;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.knockout.KnockoutMatchQualifier;
import com.ab.worldcup.knockout.KnockoutService;
import com.ab.worldcup.knockout.KnockoutTeam;
import com.ab.worldcup.match.*;
import com.ab.worldcup.results.CalculatedUserBet;
import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.results.Qualifier;
import com.ab.worldcup.results.ResultsService;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.web.model.BetData;
import com.ab.worldcup.web.model.BetOverviewData;
import com.ab.worldcup.web.model.MatchesData;
import com.ab.worldcup.web.model.UserBetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private KnockoutService knockoutService;

    @Autowired
    private ResultsService resultsService;

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
        List<UserBet> userBets = userBetRepository.findByUserBetIdAccountId(accountId);
        // all matches with results added
        List<UserBet> userMatchTypeBets = userBets.stream().filter(t -> t.getMatchId() != null).collect(Collectors.toList());
        List<? extends Match> allMatches = addUserBetsToMatches(matchService.getAllMatches(), userMatchTypeBets);
        // filter group stage matches
        List<GroupMatch> groupMatches = allMatches.stream()
                .filter(m -> m.getStageId().equals(Stage.GROUP))
                .map(m -> (GroupMatch) m)
                .collect(Collectors.toList());
        // filter knockout stage matches
        List<KnockoutMatch> knockoutMatches = allMatches.stream()
                .filter(m -> !m.getStageId().equals(Stage.GROUP))
                .map(m -> (KnockoutMatch) m)
                .collect(Collectors.toList());

        Set<KnockoutTeam> knockoutTeams = calculateKnockoutTeams(allMatches, userMatchTypeBets);
        List<KnockoutMatch> allKnockoutMatch = addKnockoutTeamsOnKnockoutMatch(knockoutMatches, knockoutTeams);

        List<Qualifier> allQualifiers = getQualifiers(allKnockoutMatch);
        Map<Stage, List<Qualifier>> qualifiersMap = allQualifiers.stream().collect(Collectors.groupingBy(Qualifier::getStageId));

        return MatchesData.builder().firstStage(groupMatches).secondStage(allKnockoutMatch).qualifiers(qualifiersMap).build();
    }

    private <T extends Match> List<T> addUserBetsToMatches(List<T> matches, List<UserBet> userBets) {
        Map<Long, UserBet> resultMap = userBets.stream().collect(Collectors.toMap(UserBet::getMatchId, Function.identity()));
        matches.forEach(match -> match.setResult(resultMap.get(match.getMatchId())));
        return matches;
    }

    @SuppressWarnings("unchecked")
    private Set<KnockoutTeam> calculateKnockoutTeams(List<? extends Match> matches, List<UserBet> results) {
        Set<KnockoutTeam> knockoutTeams = new HashSet<>();
        matches.forEach(match -> {
            List<KnockoutTeam> knockoutTeamUpdatedByMatch = knockoutService.getKnockoutTeamUpdatedByMatch(match, results);
            knockoutTeams.addAll(knockoutTeamUpdatedByMatch);
        });
        return knockoutTeams;
    }

    private List<Qualifier> getQualifiers(List<KnockoutMatch> knockoutMatches) {
        List<Qualifier> qualifiers = new ArrayList<>();
        for (KnockoutMatch knockoutMatch : knockoutMatches) {
            if (knockoutMatch.getHomeTeam() != null) {
                qualifiers.add(Qualifier.builder()
                        .team(knockoutMatch.getHomeTeam())
                        .stageId(knockoutMatch.getStageId())
                        .knockoutTeamCode(knockoutMatch.getHomeTeamCode())
                        .build());
            }
            if (knockoutMatch.getAwayTeam() != null) {
                qualifiers.add(Qualifier.builder()
                        .team(knockoutMatch.getAwayTeam())
                        .stageId(knockoutMatch.getStageId())
                        .knockoutTeamCode(knockoutMatch.getAwayTeamCode())
                        .build());
            }

            if (knockoutMatch.getResult() != null) {
                if (knockoutMatch.getStageId().equals(Stage.FINAL) || knockoutMatch.getStageId().equals(Stage.THIRD_PLACE)) {
                    qualifiers.add(Qualifier.builder()
                            .team(knockoutMatch.getResult().getKnockoutQualifier())
                            .stageId(knockoutMatch.getStageId().getNextStage().get(0))
                            .knockoutTeamCode(knockoutMatch.getStageId().equals(Stage.FINAL) ? KnockoutTeamCode.WINNER_FINAL : KnockoutTeamCode.WINNER_THIRD_PLACE)
                            .build());
                }
            }
        }
        return qualifiers;
    }

    private List<KnockoutMatch> addKnockoutTeamsOnKnockoutMatch(List<KnockoutMatch> knockoutMatchList, Set<KnockoutTeam> knockoutTeams) {
        Map<Long, KnockoutTeam> knockoutTeamByMatchId = knockoutTeams.stream().collect(Collectors.toMap(KnockoutTeam::getMatchId, Function.identity()));
        knockoutMatchList.forEach(match -> match.setKnockoutTeam(knockoutTeamByMatchId.get(match.getMatchId())));
        return knockoutMatchList;
    }

    // **************************  User Bet ***********************************

    public void deleteUserBet(Long accountId, Long betId) {
        UserBet userBer = userBetRepository.findByUserBetIdAccountIdAndUserBetIdBetId(accountId, betId);
        if (userBer == null) {
            throw new IllegalArgumentException("User bet not found");
        }
        // validate if changing may effect other bets
        Match match = matchService.getMatchById(userBer.getMatchId());
        List<UserBet> userBets = userBetRepository.findByUserBetIdAccountId(accountId);
        List<UserBet> userMatchTypeBets = userBets.stream().filter(t -> t.getMatchId() != null).collect(Collectors.toList());
        if (knockoutService.isResultsEffected(match, userMatchTypeBets)) {
            throw new IllegalArgumentException("Bet cannot be changed, it may effect other bets");
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
        // validate if changing may effect other bets
        List<UserBet> userBets = userBetRepository.findByUserBetIdAccountId(account.getId());
        List<UserBet> userMatchTypeBets = userBets.stream().filter(t -> t.getMatchId() != null).collect(Collectors.toList());
        if (knockoutService.isResultsEffected(match, userMatchTypeBets)) {
            throw new IllegalArgumentException("Bet cannot be changed, it may effect other bets");
        }

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
            if (userBetData.getMatchQualifier().equals(KnockoutMatchQualifier.HOME_TEAM)) {
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

    public List<BetOverviewData> getOverview(Account account) {
        List<BetOverviewData> betOverviewData = new ArrayList<>();

        List<Bet> bets = betRepository.findAll();
        List<Qualifier> allQualifiers = resultsService.getAllQualifiers();
        List<UserBet> betForAccount = findByUserBetIdAccountId(account.getId());

        // matches map by match id
        Map<Long, ? extends Match> matchesMap = matchService.getAllMatches().stream()
                .collect(Collectors.toMap(Match::getMatchId, Function.identity()));
        // match results by match id
        Map<Long, MatchResult> resultMap = resultsService.getAllMatchResults().stream()
                .collect(Collectors.toMap(MatchResult::getMatchId, Function.identity()));
        // user bets by match id
        Map<Long, UserBet> userBetMap = betForAccount.stream().filter(b->b.getUserBetId().getBet().getType().equals(BetType.MATCH))
                .collect(Collectors.toMap(b -> b.getUserBetId().getBet().getMatchId(), Function.identity()));
        // calculated results by bet id
        Map<Long, CalculatedUserBet> betMap = resultsService.calculateBetsForUser(betForAccount).stream()
                .collect(Collectors.toMap(b -> b.getUserBet().getUserBetId().getBet().getId(), Function.identity()));


        bets.forEach(bet -> {

            BetData betData = BetData.builder()
                    .id(bet.getId())
                    .matchId(bet.getMatchId())
                    .description(bet.getDescription())
                    .lockTime(bet.getLockTime())
                    .stageId(bet.getStageId())
                    .type(bet.getType())
                    .build();


            if (bet.getType().equals(BetType.MATCH)) {
                betOverviewData.add(BetOverviewData.builder()
                        .betData(betData)
                        .match(matchesMap.get(bet.getMatchId()))
                        .matchResult(resultMap.get(bet.getMatchId()))
                        .userBet(userBetMap.get(bet.getMatchId()))
                        .calculatedUserBet(betMap.get(bet.getId())).build());
            } else /*if (bet.getType().equals(BetType.QUALIFIER)) */ {

                betOverviewData.add(BetOverviewData.builder()
                        .betData(betData)
                        .calculatedUserBet(betMap.get(bet.getId())).build());

            }
        });

        return betOverviewData;
    }


    public void setQualifiersBets(Account account) {
        Map<Stage, List<Qualifier>> userQualifiersByStageMap = getMatchesData(account.getId()).getQualifiers();
        List<Bet> qualifierBets = betRepository.findAllByType(BetType.QUALIFIER);
        Map<Stage, List<Bet>> stageListMap = qualifierBets.stream().collect(Collectors.groupingBy(Bet::getStageId));

        userQualifiersByStageMap.forEach((stage, userQualifierList) -> {
            //if (userQualifierList != null && !userQualifierList.isEmpty()) {
            List<Bet> qualifierBetsByStage = stageListMap.get(stage);
            int qualifierBetsByStageSize = qualifierBetsByStage.size();
            int qualifierIndex = 0;
            for (Qualifier userQualifier : userQualifierList) {
                if (qualifierBetsByStageSize > qualifierIndex) {
                    Bet bet = qualifierBetsByStage.get(qualifierIndex++);
                    UserBet userBet = userBetRepository.findByUserBetIdAccountIdAndUserBetIdBetId(account.getId(), bet.getId());
                    if (userBet == null) {
                        userBet = new UserBet(new UserBetId(account, bet));
                    }
                    userBet.setQualifier(userQualifier.getTeam());
                    userBetRepository.save(userBet);
                }
            }
            //}
        });
    }
}
