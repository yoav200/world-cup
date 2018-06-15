package com.ab.worldcup.web.api;

import com.ab.worldcup.ranking.RankingDataNew;
import com.ab.worldcup.ranking.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @RequestMapping("/")
    public List<RankingDataNew> getLeaderboard() {
        return rankingService.getLeaderboard();
    }
}
