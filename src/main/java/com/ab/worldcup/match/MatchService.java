package com.ab.worldcup.match;

import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.knockout.KnockoutService;
import com.ab.worldcup.knockout.KnockoutTeam;
import com.ab.worldcup.results.*;
import com.ab.worldcup.team.Group;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
    @CacheEvict(cacheNames = "CalculatedUserBets")
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
}
