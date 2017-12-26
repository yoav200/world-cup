package com.ab.worldcup.results;

import com.ab.worldcup.bet.BetType;
import com.ab.worldcup.bet.UserBet;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CalculatedUserBet {
    UserBet userBet;

    BetType betType;

    boolean isMatchResultCorrect;

    boolean isExactScore;

    boolean isCorrectQualifier;

    int pointsForBet;
}
