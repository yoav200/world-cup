package com.ab.worldcup.web.api;

import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.knockout.KnockoutService;
import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.match.KnockoutMatch;
import com.ab.worldcup.match.Match;
import com.ab.worldcup.match.MatchService;
import com.ab.worldcup.ranking.RankingService;
import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.results.ResultsService;
import com.ab.worldcup.web.model.MatchResultData;
import com.ab.worldcup.web.model.MatchesData;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private ResultsService resultsService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private KnockoutService<MatchResult> knockoutService;

    @Autowired
    private RankingService rankingService;

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
        List<MatchResult> allResults = resultsService.getAllMatchResults();
        return matchService.getMatchesData(allResults);
    }

    @ResponseBody
    @RequestMapping(value = "/knockout")
    public List<KnockoutMatch> getAllKnockoutMatch() {
        List<MatchResult> allResults = resultsService.getAllMatchResults();
        return matchService.addResultsToMatches(knockoutService.getAllKnockoutMatches(), allResults);
    }

    @ResponseBody
    @RequestMapping(value = "/groups")
    public List<GroupMatch> getAllGroupMatches() {
        List<MatchResult> allResults = resultsService.getAllMatchResults();
        return matchService.addResultsToMatches(groupService.getAllGroupMatches(), allResults);
    }

    // ~ ===============================  ADMIN ONLY ==========================

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping(value = "/{matchId}", method = RequestMethod.POST)
    public Match updateGroupMatches(@PathVariable Long matchId, @RequestBody @Valid MatchResultData matchResultData) {
        Match match = matchService.updateMatchResult(matchId, matchResultData);
        matchService.onMatchFinish(match);
        // trigger ranking creation - this is done async
        rankingService.createRankingAsync(LocalDateTime.now());

        return match;
    }
}
