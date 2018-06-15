package com.ab.worldcup.web.model;

import com.ab.worldcup.account.Account;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RankingData {

    Account account;

    Integer totalPoints;

    Integer qualifierBetCompletionPercentage;

    Integer groupMatchesBetCompletionPercentage;

    Integer knockoutMatchesBetCompletionPercentage;

    //List<CalculatedUserBet> userBets;

}
