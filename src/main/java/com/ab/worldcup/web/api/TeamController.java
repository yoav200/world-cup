package com.ab.worldcup.web.api;

import com.ab.worldcup.team.Team;
import com.ab.worldcup.team.TeamRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    TeamRepository teamRepository;


    @RequestMapping("/")
    public List<Team> getTeams() {
        return teamRepository.findAll();
    }

}
