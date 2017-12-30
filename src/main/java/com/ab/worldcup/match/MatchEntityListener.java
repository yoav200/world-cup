package com.ab.worldcup.match;


import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityListeners;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@EntityListeners(Match.class)
public class MatchEntityListener {

    @Autowired
    private MatchService matchService;

    @PostPersist
    @PostUpdate
    public void matchFinished(Match match) {
        matchService.onMatchFinish(match);
    }
}
