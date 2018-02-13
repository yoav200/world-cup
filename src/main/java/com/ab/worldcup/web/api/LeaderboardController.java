package com.ab.worldcup.web.api;

import com.ab.worldcup.results.ResultsService;
import com.ab.worldcup.web.model.RankingData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class LeaderboardController {

    @Autowired
    ResultsService resultsService;

    @RequestMapping("/")
    public List<RankingData> getLeaderboard() {
        return resultsService.getLeaderboard();
    }
}
