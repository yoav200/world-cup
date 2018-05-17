package com.ab.worldcup.web.model;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.results.CalculatedUserBet;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RankingData {

    Account account;

    Integer totalPoints;

    Integer betCompletionPercentage;

    List<CalculatedUserBet> userBets;


}
