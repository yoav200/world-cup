package com.ab.worldcup.web.api;

import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.knockout.KnockoutService;
import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.match.KnockoutMatch;
import com.ab.worldcup.match.Match;
import com.ab.worldcup.match.MatchService;
import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.web.model.MatchResultData;
import com.ab.worldcup.web.model.MatchesData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    @Qualifier("MatchResultDataValidator")
    private Validator validator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @ResponseBody
    @RequestMapping(value = "/data")
    public MatchesData getMatchesData() {
        return matchService.getMatchesData();
    }

    @ResponseBody
    @RequestMapping(value = "/knockout")
    public List<KnockoutMatch> getAllKnockoutMatch() {
        return matchService.addResultsToMatches(knockoutService.getAllKnockoutMatches());
    }

    @ResponseBody
    @RequestMapping(value = "/groups")
    public List<GroupMatch> getAllGroupMatches() {
        return matchService.addResultsToMatches(groupService.getAllGroupMatches());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping(value = "/{matchId}", method = RequestMethod.POST)
    public Match updateGroupMatches(@PathVariable Long matchId, @RequestBody @Valid MatchResultData matchResultData) {
        Match match = matchService.updateMatchResult(matchId, matchResultData);
        matchService.onMatchFinish(match);
        return match;
    }
}
