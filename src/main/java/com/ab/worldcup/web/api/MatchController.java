package com.ab.worldcup.web.api;

import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.knockout.KnockoutService;
import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.match.KnockoutMatch;
import com.ab.worldcup.match.Match;
import com.ab.worldcup.match.MatchService;
import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.web.model.MatchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private KnockoutService<MatchResult> knockoutService;


    @ResponseBody
    @RequestMapping(value = "/")
    public List<MatchData> getAll() {
        return matchService.getMatchData();
    }

    @ResponseBody
    @RequestMapping(value = "/knockout")
    public Set<KnockoutMatch> getAllKnockoutMatch() {
        return knockoutService.getAllKnockoutMatches();
    }


    @ResponseBody
    @RequestMapping(value = "/groups")
    public Set<GroupMatch> getAllGroupMatches() {
        return groupService.getAllGroupMatches();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping(value = "/groups/{matchId}", method = RequestMethod.POST)
    public Match updateGroupMatches(@PathVariable Long matchId, @RequestBody MatchResult matchResult) {
        Match groupMatch = matchService.updateGroupMatchResult(matchId, matchResult);
        matchService.onMatchFinish(groupMatch);
        return groupMatch;
    }


}
