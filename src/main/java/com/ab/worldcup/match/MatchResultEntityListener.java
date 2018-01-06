package com.ab.worldcup.match;


import com.ab.worldcup.BeanUtil;
import com.ab.worldcup.results.MatchResult;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

public class MatchResultEntityListener {


    @PostPersist
    @PostUpdate
    public void matchFinished(MatchResult matchResult) {
        MatchService matchService = BeanUtil.getBean(MatchService.class);
        Match match = matchService.getMatchById(matchResult.getMatchId());
        matchService.onMatchFinish(match);
    }
}
