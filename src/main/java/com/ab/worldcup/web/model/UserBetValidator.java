package com.ab.worldcup.web.model;

import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetType;
import com.ab.worldcup.bet.UserBet;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserBetValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserBet.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserBet userBet = (UserBet) target;

        //if (userBet.getUserBetId() == null || userBet.getUserBetId().getBet() == null || userBet.getUserBetId().getAccount() == null) {
        //    errors.rejectValue("userBetId", "userBet.id", "Bet missing key data");
        //} else {

            Bet bet = userBet.getUserBetId().getBet();
            if (bet.getType().equals(BetType.MATCH)) {
                if (userBet.getHomeTeam() == null
                        || userBet.getAwayTeam() == null
                        || userBet.getHomeTeamGoals() == null
                        || userBet.getAwayTeamGoals() == null) {
                    errors.rejectValue("userBet", "userBet.team.data", "Bet missing team data");
                }
            }
            if (bet.getType().equals(BetType.QUALIFIER)) {
                if (userBet.getQualifier() == null) {
                    errors.rejectValue("userBet", "userBet.team.qualifier", "Bet missing qualifier data");
                }
            }

        //}
    }
}
