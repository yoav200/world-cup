package com.ab.worldcup.web.api;

import com.ab.worldcup.league.LeagueService;
import com.ab.worldcup.ranking.RankingDataNew;
import com.ab.worldcup.ranking.RankingService;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ranking")
public class RankingController {

  private final RankingService rankingService;
  private final LeagueService leagueService;

  @RequestMapping("/")
  public List<RankingDataNew> getLeaderboard(@RequestParam(required = false) Long leagueId) {
    List<RankingDataNew> leaderboard = rankingService.getLeaderboard();
    if (leagueId != null) {
      Set<Long> memberIds = Set.copyOf(leagueService.getLinkedAccountIds(leagueId));
      leaderboard = leaderboard.stream()
          .filter(r -> memberIds.contains(r.getAccount().getId()))
          .toList();
      // Re-rank within the league
      int rank = 1;
      Integer lastPoints = null;
      for (int i = 0; i < leaderboard.size(); i++) {
        RankingDataNew r = leaderboard.get(i);
        if (lastPoints != null && !lastPoints.equals(r.getPoints())) {
          rank = i + 1;
        }
        lastPoints = r.getPoints();
        r.setRank(rank);
      }
    }
    return leaderboard;
  }
}
