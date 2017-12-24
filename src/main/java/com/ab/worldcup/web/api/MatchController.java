package com.ab.worldcup.web.api;

import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.match.GroupMatchRepository;
import com.ab.worldcup.match.KnockoutMatch;
import com.ab.worldcup.match.KnockoutMatchRepository;
import com.ab.worldcup.team.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    @Autowired
    private GroupMatchRepository groupMatchRepository;

    @Autowired
    KnockoutMatchRepository knockoutMatchRepository;

    @ResponseBody
    @RequestMapping(value = "/groups")
    List<GroupMatch> getAllGroupMatches() {
        return  groupMatchRepository.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "/groups/{group}")
    List<GroupMatch> getGroupMatches(@PathVariable Group group) {
        return  groupMatchRepository.findByGroupId(group);
    }


    @ResponseBody
    @RequestMapping(value = "/knockout")
    List<KnockoutMatch> getAllKnockoutMatch() {
        return  knockoutMatchRepository.findAll();
    }
}
