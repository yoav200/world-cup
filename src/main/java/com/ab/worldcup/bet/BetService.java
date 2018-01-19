package com.ab.worldcup.bet;

import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.knockout.KnockoutService;
import com.ab.worldcup.knockout.KnockoutTeam;
import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.match.KnockoutMatch;
import com.ab.worldcup.match.Match;
import com.ab.worldcup.match.Stage;
import com.ab.worldcup.results.Qualifier;
import com.ab.worldcup.team.Team;
import com.ab.worldcup.web.model.MatchesData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BetService {

    @Autowired
    private BetRepository betRepository;

    @Autowired
    private UserBetRepository userBetRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private KnockoutService knockoutService;


    /**
     * This method will return all UserBet in which <code>team</code> qualified to <code>stage</code>
     *
     * @return List<UserBet>
     */
    public List<UserBet> getAllCorrectUserBetForQualifiedTeam(Team team, Stage stage) {
        List<Bet> allQualificationBetsForThisStage = betRepository.findAllByStageIdAndType(stage, BetType.QUALIFIER);
        List<UserBet> correctUserBets = new ArrayList<>();
        for (Bet bet : allQualificationBetsForThisStage) {
            correctUserBets.addAll(userBetRepository.findByUserBetIdBetAndQualifier(bet, team));
        }
        return correctUserBets;
    }

    @Cacheable("allBets")
    public List<Bet> getAllBets() {
        return betRepository.findAll();
    }

    public List<UserBet> findByUserBetIdAccountId(Long accountId) {
        return userBetRepository.findByUserBetIdAccountId(accountId);
    }

    public UserBet setUserBet(UserBet userBet) {
        return null;
    }

    public MatchesData getMatchesData(Long accountId) {
        List<UserBet> userBets = userBetRepository.findByUserBetIdAccountId(accountId);
        List<GroupMatch> allGroupMatches = addUserBetsToMatches(groupService.getAllGroupMatches(), userBets);
        List<KnockoutMatch> allKnockoutMatch = addUserBetsToMatches(knockoutService.getAllKnockoutMatches(), userBets);

        List<Qualifier> allQualifiers = getAllQualifiers(userBets);

        Map<Stage, List<Qualifier>> map = allQualifiers.stream().collect(Collectors.groupingBy(Qualifier::getStageId));

        return MatchesData.builder().firstStage(allGroupMatches).secondStage(allKnockoutMatch).qualifiers(map).build();
    }

    private List<Match> addBetsToMatchs(List<Match> matches) {
        List<UserBet> userBets = userBetRepository.findAll();
        Map<Long, UserBet> betsMap = userBets.stream()
                .filter(b -> b.getUserBetId().getBet().getMatchId() != null)
                .collect(Collectors.toMap(b -> b.getUserBetId().getBet().getMatchId(), Function.identity()));
        matches.forEach(match -> match.setResult(betsMap.get(match.getMatchId())));
        return matches;
    }

    private <T extends Match> List<T> addUserBetsToMatches(List<T> matches, List<UserBet> userBets) {
        Map<Long, UserBet> betMap = userBets.stream().collect(Collectors.toMap(UserBet::getMatchId, Function.identity()));
        matches.forEach(match -> match.setResult(betMap.get(match.getMatchId())));
        return matches;
    }

    private List<Qualifier> getAllQualifiers(List<UserBet> userBets) {
        List<Qualifier> list = new ArrayList<>();

        List<? extends Match> allGroupMatches = groupService.getAllGroupMatches();
        List<? extends Match> allKnockoutMatches = knockoutService.getAllKnockoutMatches();

        Stream.concat(allGroupMatches.stream(), allKnockoutMatches.stream()).forEach(match -> {
            List<KnockoutTeam> knockoutTeamUpdatedByMatch = knockoutService.getKnockoutTeamUpdatedByMatch(match, userBets);
            for (KnockoutTeam teamUpdatedByMatch : knockoutTeamUpdatedByMatch) {
                if (teamUpdatedByMatch.getHomeTeam() != null) {
                    Qualifier qualifier = Qualifier.builder()
                            .team(teamUpdatedByMatch.getHomeTeam())
                            .stageId(match.getStageId().getNextStage())
                            .knockoutTeamCode(teamUpdatedByMatch.getKnockoutMatch().getHomeTeamCode())
                            .build();
                    list.add(qualifier);
                }
                if (teamUpdatedByMatch.getAwayTeam() != null) {
                    Qualifier qualifier = Qualifier.builder()
                            .team(teamUpdatedByMatch.getAwayTeam())
                            .stageId(match.getStageId().getNextStage())
                            .knockoutTeamCode(teamUpdatedByMatch.getKnockoutMatch().getAwayTeamCode())
                            .build();
                    list.add(qualifier);
                }
            }
        });
        return list;
    }
}
