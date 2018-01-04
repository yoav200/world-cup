package com.ab.worldcup.match;


import com.ab.worldcup.results.MatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityListeners;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Component
@EntityListeners(MatchResult.class)
public class MatchResultEntityListener {

    @Autowired
    private MatchService matchService;


    @PostPersist
    @PostUpdate
    public void matchFinished(MatchResult matchResult) {
        Match match = matchService.getMatchById(matchResult.getMatchId());
        matchService.onMatchFinish(match);
    }
}
