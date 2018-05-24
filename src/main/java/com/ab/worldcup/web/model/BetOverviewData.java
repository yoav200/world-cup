package com.ab.worldcup.web.model;

import com.ab.worldcup.bet.UserBet;
import com.ab.worldcup.match.Match;
import com.ab.worldcup.results.CalculatedUserBet;
import com.ab.worldcup.results.MatchResult;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BetOverviewData {
    private Match  match;
    private MatchResult matchResult;
    private UserBet userBet;
    private CalculatedUserBet calculatedUserBet;
}
