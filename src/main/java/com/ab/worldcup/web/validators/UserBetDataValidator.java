package com.ab.worldcup.web.validators;

import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.bet.BetType;
import com.ab.worldcup.web.model.UserBetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("UserBetDataValidator")
public class UserBetDataValidator implements Validator {

    private final BetService betService;

    @Autowired
    public UserBetDataValidator(BetService betService) {
        this.betService = betService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserBetData.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserBetData userBetData = (UserBetData) target;

        Bet bet = betService.getBetById(userBetData.getBetId());

        if (bet == null) {
            errors.rejectValue("betId", "", "Bet not found");
        } else {
            if ((userBetData.getHomeTeamGoals() == null || userBetData.getHomeTeamGoals() < 0)) {
                errors.rejectValue("homeTeamGoals", "", "Home team goals result must be set");
            }

            if ((userBetData.getAwayTeamGoals() == null || userBetData.getAwayTeamGoals() < 0)) {
                errors.rejectValue("awayTeamGoals", "", "Away team goals result must be set");
            }

            if (bet.getType().equals(BetType.QUALIFIER)) {
                if (userBetData.getMatchQualifier() == null) {
                    errors.rejectValue("qualifier", "userBet.team.qualifier", "Bet missing qualifier");
                }
            }
        }
    }
}
