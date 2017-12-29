package com.ab.worldcup.web.api;

import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.match.KnockoutMatch;
import com.ab.worldcup.match.KnockoutMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    @Autowired
    private GroupService groupService;

    @Autowired
    KnockoutMatchRepository knockoutMatchRepository;

    @ResponseBody
    @RequestMapping(value = "/groups")
    Set<GroupMatch> getAllGroupMatches() {
        return groupService.getAllGroupMatchs();
    }


    @ResponseBody
    @RequestMapping(value = "/knockout")
    List<KnockoutMatch> getAllKnockoutMatch() {
        return knockoutMatchRepository.findAll();
    }
}
