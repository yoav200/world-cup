package com.ab.worldcup.bet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BetStatisticsData {
    int betsOnHomeTeamPercent;
    int betsOnAwayTeamPercent;
    int betsOnDrawPercent;
}
