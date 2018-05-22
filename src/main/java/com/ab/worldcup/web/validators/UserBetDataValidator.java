package com.ab.worldcup.web.validators;

import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.match.MatchService;
import com.ab.worldcup.web.model.UserBetData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component("UserBetDataValidator")
public class UserBetDataValidator implements Validator {

    private final BetService betService;

    private final MatchService matchService;

    @Autowired
    public UserBetDataValidator(BetService betService, MatchService matchService) {
        this.betService = betService;
        this.matchService = matchService;
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
            if (LocalDateTime.now().isAfter(bet.getLockTime().toLocalDateTime())) {
                errors.rejectValue("betId", "", "Cannot bet on match that already started");
            }
            // validate data
            userBetData.validate(errors, bet.getStageId());
        }
    }
}
