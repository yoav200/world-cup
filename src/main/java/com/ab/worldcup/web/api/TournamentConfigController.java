package com.ab.worldcup.web.api;

import com.ab.worldcup.tournament.TournamentConfigService;
import com.ab.worldcup.web.model.TournamentConfig;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tournament")
public class TournamentConfigController {

  private final TournamentConfigService tournamentConfigService;

  @RequestMapping("/config")
  public TournamentConfig getTournamentConfig() {
    return tournamentConfigService.getTournamentConfig();
  }
}
