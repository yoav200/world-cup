package com.ab.worldcup.match;

import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.knockout.KnockoutService;
import com.ab.worldcup.knockout.KnockoutTeam;
import com.ab.worldcup.results.*;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.Team;
import com.ab.worldcup.web.model.MatchData;
import com.google.common.collect.Lists;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Log
@Service
public class MatchService {

    @Autowired
    private KnockoutMatchRepository knockoutMatchRepository;

    @Autowired
    private MatchResultRepository matchResultRepository;

    @Autowired
    private GroupMatchRepository groupMatchRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private KnockoutService knockoutService;

    @Autowired
    private BetService betService;

    @Autowired
    private ResultsService resultService;

    /**
     * Call this method only after persisting the result of <code>match</code>
     * this method will:
     * 1. update the relevant KnockoutTeam record if needed
     * 2. will add a record to qualifier table if needed
     */
    @CacheEvict(cacheNames = {"CalculatedUserBets", "knockoutMatches", "groupMatches"})
    public void onMatchFinish(Match match) {
        List<KnockoutTeam> knockoutTeamUpdatedByMatch = getKnockoutTeamUpdatedByMatch(match);
        for (KnockoutTeam teamUpdatedByMatch : knockoutTeamUpdatedByMatch) {
            if (teamUpdatedByMatch.getHomeTeam() != null) {
                Qualifier qualifier = Qualifier.builder()
                        .team(teamUpdatedByMatch.getHomeTeam())
                        .stageId(getMatchStage(match).getNextStage())
                        .knockoutTeamCode(teamUpdatedByMatch.getKnockoutMatch().getHomeTeamCode())
                        .build();
                resultService.saveQualifier(qualifier);
            }
            if (teamUpdatedByMatch.getAwayTeam() != null) {
                Qualifier qualifier = Qualifier.builder()
                        .team(teamUpdatedByMatch.getAwayTeam())
                        .stageId(getMatchStage(match).getNextStage())
                        .knockoutTeamCode(teamUpdatedByMatch.getKnockoutMatch().getAwayTeamCode())
                        .build();
                resultService.saveQualifier(qualifier);
            }
            resultService.saveKnockoutTeam(teamUpdatedByMatch);
        }
    }


    private <T extends ResultInterface> List<KnockoutTeam> getKnockoutTeamUpdatedByMatch(Match match) {
        List<KnockoutTeam> knockoutTeamList = new ArrayList<>();
        List<T> matchResultList = (List<T>) matchResultRepository.findAll();
        Stage currentStage = getMatchStage(match);
        boolean groupFinished = false;

        if (Stage.GROUP.equals(currentStage)) {
            Group group = groupService.getGroupIdByMatchId(match.getMatchId());
            groupFinished = groupService.isGroupFinished(group, matchResultList);
        }

        if (groupFinished || !Stage.GROUP.equals(currentStage)) {
            Stage nextStage = currentStage.getNextStage();
            List<KnockoutMatch> knockoutMatchesInNextStage = knockoutMatchRepository.findAllByStageId(nextStage);
            for (KnockoutMatch knockoutMatch : knockoutMatchesInNextStage) {
                Optional<KnockoutTeam> knockoutTeam = knockoutService.getKnockoutTeamForKnockoutMatch(knockoutMatch, matchResultList);
                knockoutTeam.ifPresent(knockoutTeamList::add);
            }
        }
        return knockoutTeamList;
    }

    private Stage getMatchStage(Match match) {
        Bet betForMatch = betService.getBetByMatch(match);
        return betForMatch.getStageId();
    }

    // TODO: 30/12/2017 do we support update result for ongoing games?
    public Match updateGroupMatchResult(Long matchId, MatchResult matchResult) {
        MatchResult result = matchResultRepository.findOne(matchId);
        if (result == null) {
            result = new MatchResult();
            result.setMatchId(matchId);
        }
        if (result.equals(matchResult)) {
            result.setHomeTeamGoals(matchResult.getHomeTeamGoals());
            result.setAwayTeamGoals(matchResult.getAwayTeamGoals());



        } else {
            throw new IllegalArgumentException("Match result do not match");
        }
        matchResultRepository.save(matchResult);

        Match groupMatch = getMatchById(matchId);
        groupMatch.setResult(result);
        return groupMatch;
    }

    public Match getMatchById(Long matchId) {
        Match one = groupMatchRepository.findOne(matchId);
        if (one == null) {
            one = knockoutMatchRepository.findOne(matchId);
        }
        return one;
    }


    public List<MatchData> getMatchData() {
        List<MatchData> list = Lists.newArrayList();
        Set<GroupMatch> allGroupMatches = groupService.getAllGroupMatches();
        Set<KnockoutMatch> allKnockoutMatch = knockoutService.getAllKnockoutMatches();
        List<MatchResult> allMatchResults = resultService.getAllMatchResults();

        Map<Long, MatchResult> resultMap = allMatchResults.stream().collect(Collectors.toMap(MatchResult::getMatchId, Function.identity()));

        allGroupMatches.forEach(match -> list.add(
                MatchData.builder()
                        .matchId(match.getMatchId())
                        .kickoff(match.getKickoff())
                        .awayTeam(match.getAwayTeam())
                        .homeTeam(match.getHomeTeam())
                        .stage(Stage.GROUP.toString() + " " + match.getGroupId().toString())
                        .result(resultMap.get(match.getMatchId())).build()));


        allKnockoutMatch.forEach(match -> {
            Optional<Team> homeTeam = knockoutService.getKnockoutTeamByTeamCode(match.getHomeTeamCode(), allMatchResults);
            Optional<Team> awayTeam = knockoutService.getKnockoutTeamByTeamCode(match.getAwayTeamCode(), allMatchResults);
            list.add(
                    MatchData.builder()
                            .matchId(match.getMatchId())
                            .kickoff(match.getKickoff())
                            .homeTeam(homeTeam.orElse(null))
                            .awayTeam(awayTeam.orElse(null))
                            .stage(match.getStageId().toString())
                            .result(resultMap.get(match.getMatchId())).build());
        });

        return list;
    }

}
