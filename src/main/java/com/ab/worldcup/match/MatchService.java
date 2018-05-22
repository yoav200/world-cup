package com.ab.worldcup.match;

import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.knockout.KnockoutService;
import com.ab.worldcup.knockout.KnockoutTeam;
import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.results.Qualifier;
import com.ab.worldcup.results.ResultsService;
import com.ab.worldcup.web.model.MatchResultData;
import com.ab.worldcup.web.model.MatchesData;
import lombok.extern.java.Log;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Log
@Service
public class MatchService {

    @Autowired
    private KnockoutMatchRepository knockoutMatchRepository;

    @Autowired
    private GroupMatchRepository groupMatchRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private KnockoutService knockoutService;

    @Autowired
    private ResultsService resultService;

    /**
     * Call this method only after persisting the result of <code>match</code>
     * this method will:
     * 1. update the relevant KnockoutTeam record if needed
     * 2. will add a record to qualifier table if needed
     */
    @SuppressWarnings("unchecked")
    @CacheEvict(cacheNames = {"allKnockoutMatchesCache", "allMatchResultsCache", "allQualifiersCache"}, allEntries = true)
    public void onMatchFinish(Match match) {
        List<MatchResult> matchResults = resultService.getAllMatchResults();
        List<KnockoutTeam> knockoutTeamUpdatedByMatch = knockoutService.getKnockoutTeamUpdatedByMatch(match, matchResults);
        for (KnockoutTeam teamUpdatedByMatch : knockoutTeamUpdatedByMatch) {
            KnockoutMatch knockoutMatch = knockoutMatchRepository.findOne(teamUpdatedByMatch.getMatchId());
            if (teamUpdatedByMatch.getHomeTeam() != null) {
                Qualifier qualifier = Qualifier.builder()
                        .team(teamUpdatedByMatch.getHomeTeam())
                        .stageId(knockoutMatch.getStageId())
                        .knockoutTeamCode(knockoutMatch.getHomeTeamCode())
                        .build();
                resultService.saveQualifier(qualifier);
            }
            if (teamUpdatedByMatch.getAwayTeam() != null) {
                Qualifier qualifier = Qualifier.builder()
                        .team(teamUpdatedByMatch.getAwayTeam())
                        .stageId(knockoutMatch.getStageId())
                        .knockoutTeamCode(knockoutMatch.getAwayTeamCode())
                        .build();
                resultService.saveQualifier(qualifier);
            }
            resultService.saveKnockoutTeam(teamUpdatedByMatch);
        }
    }

    public Match updateMatchResult(Long matchId, MatchResultData matchResult) {
        // get related match
        Match match = getMatchById(matchId);

        MatchResult result = resultService.findMatchResultByMatchId(match.getMatchId());
        if (result == null) {
            result = new MatchResult();
            result.setMatchId(match.getMatchId());
        }
        // set goals
        result.setHomeTeamGoals(matchResult.getHomeTeamGoals());
        result.setAwayTeamGoals(matchResult.getAwayTeamGoals());
        // knockout match
        if (!Stage.GROUP.equals(match.getStageId())) {
            result.setHomeTeam(groupService.findTeamByCode(matchResult.getHomeTeamCode()));
            result.setAwayTeam(groupService.findTeamByCode(matchResult.getAwayTeamCode()));
            result.setMatchQualifier(matchResult.getMatchQualifier());
        } else {
            result.setHomeTeam(match.getHomeTeam());
            result.setAwayTeam(match.getAwayTeam());
        }
        // save the result
        resultService.save(result);

        return getMatchById(match.getMatchId()).setResult(result);
    }

    public Match getMatchById(Long matchId) {
        Match one = groupMatchRepository.findOne(matchId);
        if (one == null) {
            one = knockoutMatchRepository.findOne(matchId);
        }
        return one;
    }

    @SuppressWarnings("unchecked")
    public MatchesData getMatchesData() {
        List<GroupMatch> allGroupMatches = addResultsToMatches(groupService.getAllGroupMatches());
        List<KnockoutMatch> allKnockoutMatch = knockoutService.addKnockoutTeamsOnKnockoutMatch(
                addResultsToMatches(knockoutService.getAllKnockoutMatches()));

        List<Qualifier> allQualifiers = resultService.getAllQualifiers();
        Map<Stage, List<Qualifier>> map = allQualifiers.stream().collect(Collectors.groupingBy(Qualifier::getStageId));
        return MatchesData.builder().firstStage(allGroupMatches).secondStage(allKnockoutMatch).qualifiers(map).build();
    }

    public <T extends Match> List<T> addResultsToMatches(List<T> matches) {
        List<MatchResult> allMatchResults = resultService.getAllMatchResults();
        Map<Long, MatchResult> resultMap = allMatchResults.stream().collect(Collectors.toMap(MatchResult::getMatchId, Function.identity()));
        matches.forEach(match -> match.setResult(resultMap.get(match.getMatchId())));
        return matches;
    }

    @SuppressWarnings("unchecked")
    public List<? extends Match> getAllMatches() {
        return ListUtils.union(groupService.getAllGroupMatches(), knockoutService.getAllKnockoutMatches());
    }
}
