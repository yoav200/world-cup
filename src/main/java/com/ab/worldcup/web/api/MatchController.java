package com.ab.worldcup.web.api;

import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.match.*;
import com.ab.worldcup.results.MatchResult;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
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
    KnockoutMatchRepository knockoutMatchRepository;

    @ResponseBody
    @RequestMapping(value = "/")
    public List<MatchBasicData> getAll() {
        List<MatchBasicData> list = Lists.newArrayList();
        Set<GroupMatch> allGroupMatchs = groupService.getAllGroupMatchs();
        List<KnockoutMatch> allKnockoutMatch = knockoutMatchRepository.findAll();

        allGroupMatchs.forEach(match -> list.add(MatchBasicData.builder()
                .matchId(match.getMatchId())
                .kickoff(match.getKickoff())
                .label(match.toString())
                .stage(Stage.GROUP).build()));

        allKnockoutMatch.forEach(match -> list.add(MatchBasicData.builder()
                .matchId(match.getMatchId())
                .kickoff(match.getKickoff())
                .label(match.toString())
                .stage(match.getStageId()).build()));

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
    public GroupMatch updateGroupMatches(@PathVariable Long matchId, @RequestBody MatchResult matchResult) {
        return matchService.updateGroupMatchResult(matchId, matchResult);
    }


    @ResponseBody
    @RequestMapping(value = "/knockout")
    public List<KnockoutMatch> getAllKnockoutMatch() {
        return knockoutMatchRepository.findAll();
    }

    @Data
    @Builder
    private static class MatchBasicData {
        private Long matchId;
        private Timestamp kickoff;
        private String label;
        private Stage stage;
    }
}
