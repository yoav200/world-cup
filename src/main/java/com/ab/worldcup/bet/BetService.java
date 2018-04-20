package com.ab.worldcup.bet;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.knockout.KnockoutMatchQualifier;
import com.ab.worldcup.knockout.KnockoutService;
import com.ab.worldcup.knockout.KnockoutTeam;
import com.ab.worldcup.match.*;
import com.ab.worldcup.results.Qualifier;
import com.ab.worldcup.team.Group;
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
        List<? extends Match> allMatches = addUserBetsToMatches(matchService.getAllMatches(), userBets);
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

        Set<KnockoutTeam> knockoutTeams = calculateKnockoutTeams(allMatches, userBets);
        List<KnockoutMatch> allKnockoutMatch = addKnockoutTeamsOnKnockoutMatch(knockoutMatches, knockoutTeams);

        List<Qualifier> allQualifiers = getQualifiers(knockoutTeams);
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

    private List<Qualifier> getQualifiers(Set<KnockoutTeam> knockoutTeams) {
        List<Qualifier> qualifiers = new ArrayList<>();
        for (KnockoutTeam teamUpdatedByMatch : knockoutTeams) {
            KnockoutMatch knockoutMatch = knockoutService.findKnockoutMatch(teamUpdatedByMatch.getMatchId());
            if (teamUpdatedByMatch.getHomeTeam() != null) {
                Qualifier qualifier = Qualifier.builder()
                        .team(teamUpdatedByMatch.getHomeTeam())
                        .stageId(knockoutMatch.getStageId().getNextStage())
                        .knockoutTeamCode(knockoutMatch.getHomeTeamCode())
                        .build();
                qualifiers.add(qualifier);
            }
            if (teamUpdatedByMatch.getAwayTeam() != null) {
                Qualifier qualifier = Qualifier.builder()
                        .team(teamUpdatedByMatch.getAwayTeam())
                        .stageId(knockoutMatch.getStageId().getNextStage())
                        .knockoutTeamCode(knockoutMatch.getAwayTeamCode())
                        .build();
                qualifiers.add(qualifier);
            }
            knockoutTeams.add(teamUpdatedByMatch);
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
        if (knockoutService.isResultsEffected(match, userBets)) {
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

    private UserBet saveUserBet(UserBet userBet) {
        return userBetRepository.save(userBet);
    }

    private UserBet findByUserBetIdAccountIdAndBetId(Long accountId, Long betId) {
        return userBetRepository.findByUserBetIdAccountIdAndUserBetIdBetId(accountId, betId);
    }

    public UserBet updateMatchBet(Long betId, Account account, UserBetData userBetData) {
        Bet bet = getBetById(betId);
        Match match = matchService.getMatchById(bet.getMatchId());

        // validate if changing may effect other bets
        List<UserBet> userBets = userBetRepository.findByUserBetIdAccountId(account.getId());
        if (knockoutService.isResultsEffected(match, userBets)) {
            throw new IllegalArgumentException("Bet cannot be changed, it may effect other bets");
        }

        UserBet userBet = findByUserBetIdAccountIdAndBetId(account.getId(), userBetData.getBetId());
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
        return saveUserBet(userBet);
    }
}
