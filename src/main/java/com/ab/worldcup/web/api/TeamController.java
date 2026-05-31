package com.ab.worldcup.web.api;

import com.ab.worldcup.team.Team;
import com.ab.worldcup.team.TeamRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {

  private final TeamRepository teamRepository;


  @RequestMapping("/")
  public List<Team> getTeams() {
    return teamRepository.findAll();
  }

}
