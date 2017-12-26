package com.ab.worldcup.match;

import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.knockout.KnockoutService;
import com.ab.worldcup.knockout.KnockoutTeam;
import com.ab.worldcup.results.MatchResultRepository;
import com.ab.worldcup.results.Qualifier;
import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.results.ResultsService;
import com.ab.worldcup.team.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class MatchService {

    @Autowired
    KnockoutMatchRepository knockoutMatchRepository;
    @Autowired
    MatchResultRepository matchResultRepository;

    @Autowired
    GroupService groupService;

    @Autowired
    KnockoutService knockoutService;

    @Autowired
    BetService betService;

    @Autowired
    ResultsService resultService;

    private final Logger logger = LoggerFactory.getLogger(MatchService.class);

    @CacheEvict(cacheNames="CalculatedUserBets")
    //
    /**
     * Call this method only after persisting the result of <code>match</code>
     * this method will:
     * 1. update the relevant KnockoutTeam record if needed
     * 2. will add a record to qualifier table if needed
     */
    public void onMatchFinish(Match match){
        List<KnockoutTeam> knockoutTeamUpdatedByMatch = getKnockoutTeamUpdatedByMatch(match);
        for (KnockoutTeam teamUpdatedByMatch : knockoutTeamUpdatedByMatch) {
            if(teamUpdatedByMatch.getHomeTeam() != null){
                Qualifier qualifier = Qualifier.builder().
                        team(teamUpdatedByMatch.getHomeTeam()).
                        stageId(getMatchStage(match).getNextStage()).
                        knockoutTeamCode(teamUpdatedByMatch.getMatchId().getHomeTeamCode()).build();

                resultService.saveQualifier(qualifier);
            }
            if(teamUpdatedByMatch.getAwayTeam() != null){
                Qualifier qualifier = Qualifier.builder().
                        team(teamUpdatedByMatch.getAwayTeam()).
                        stageId(getMatchStage(match).getNextStage()).
                        knockoutTeamCode(teamUpdatedByMatch.getMatchId().getAwayTeamCode()).build();

                resultService.saveQualifier(qualifier);
            }
            resultService.saveKnockoutTeam(teamUpdatedByMatch);
        }
    }


    private <T extends ResultInterface> List<KnockoutTeam> getKnockoutTeamUpdatedByMatch(Match match){
        List<KnockoutTeam> knockoutTeamList = new ArrayList<>();
        List<T> matchResultList = (List<T>) matchResultRepository.findAll();
        Stage currentStage = getMatchStage(match);
        boolean groupFinished = false;

        if(Stage.GROUP.equals(currentStage)){
            Group group = groupService.getGroupIdByMatchId(match.getMatchId());
            groupFinished = groupService.isGroupFinished(group, matchResultList);
        }

        if(groupFinished || !Stage.GROUP.equals(currentStage)) {
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

}
