package com.ab.worldcup.web.api;

import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.knockout.KnockoutService;
import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.match.KnockoutMatch;
import com.ab.worldcup.match.Match;
import com.ab.worldcup.match.MatchService;
import com.ab.worldcup.match.Stage;
import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.results.ResultsService;
import com.ab.worldcup.team.Team;
import com.ab.worldcup.web.model.MatchData;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private KnockoutService<MatchResult> knockoutService;

    @Autowired
    private ResultsService resultsService;

    @ResponseBody
    @RequestMapping(value = "/")
    public List<MatchData> getAll() {
        List<MatchData> list = Lists.newArrayList();
        Set<GroupMatch> allGroupMatches = groupService.getAllGroupMatchs();
        List<KnockoutMatch> allKnockoutMatch = knockoutService.getAllKnockoutMatches();
        List<MatchResult> allMatchResults = resultsService.getAllMatchResults();

        Map<Long, MatchResult> resultMap = allMatchResults.stream().collect(Collectors.toMap(MatchResult::getMatchId, Function.identity()));


        allGroupMatches.forEach(match -> list.add(
                MatchData.builder()
                        .matchId(match.getMatchId())
                        .kickoff(match.getKickoff())
                        .awayTeam(match.getAwayTeam())
                        .homeTeam(match.getHomeTeam())
                        .stage(Stage.GROUP)
                        .result(resultMap.get(match.getMatchId())).build()));


        allKnockoutMatch.forEach(match -> {
            Optional<Team> homeTeam = knockoutService.getKnockoutTeamByTeamCode(match.getHomeTeamCode(), allMatchResults);
            Optional<Team> awatTeam = knockoutService.getKnockoutTeamByTeamCode(match.getAwayTeamCode(), allMatchResults);
            list.add(
                    MatchData.builder()
                            .matchId(match.getMatchId())
                            .kickoff(match.getKickoff())
                            .homeTeam(homeTeam.orElse(null))
                            .awayTeam(awatTeam.orElse(null))
                            .stage(match.getStageId())
                            .result(resultMap.get(match.getMatchId())).build());
        });

        return list;
    }

    @ResponseBody
    @RequestMapping(value = "/groups")
    public Set<GroupMatch> getAllGroupMatches() {
        return groupService.getAllGroupMatchs();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping(value = "/groups/{matchId}", method = RequestMethod.POST)
    public Match updateGroupMatches(@PathVariable Long matchId, @RequestBody MatchResult matchResult) {
        Match groupMatch = matchService.updateGroupMatchResult(matchId, matchResult);
        matchService.onMatchFinish(groupMatch);
        return groupMatch;
    }


    @ResponseBody
    @RequestMapping(value = "/knockout")
    public List<KnockoutMatch> getAllKnockoutMatch() {
        return knockoutService.getAllKnockoutMatches();
    }
}
