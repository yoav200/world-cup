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

    int matchResultPoints;

    int exactScorePoints;

    int correctQualifierPoints;

    BetCorrectnessTypeEnum isCorrectQualifier;

    public boolean isMatchResultCorrect(){
        return matchResultPoints > 0;
    }

    public boolean isExactScoreCorrect(){
        return exactScorePoints > 0;
    }

    public int  getTotalPoints(){
        return matchResultPoints + exactScorePoints + correctQualifierPoints;
    }
}
