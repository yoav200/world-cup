package com.ab.worldcup.web.model;

import com.ab.worldcup.results.CalculatedUserBet;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BetOverviewData {
    private BetData betData;
    private UserBetData userBetData;
    private MatchResultData matchResultData;
    private CalculatedUserBet calculatedUserBet;
}
