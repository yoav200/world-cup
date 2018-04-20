package com.ab.worldcup.web.validators;

import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.match.*;
import com.ab.worldcup.team.Group;
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
            // validate data
            userBetData.validate(errors, bet.getStageId());
            // validate match
            Match match = matchService.getMatchById(bet.getMatchId());

            if (LocalDateTime.now().isAfter(match.getKickoff().toLocalDateTime())) {
                errors.rejectValue("betId", "", "Cannot bet on match that already started");
            }

            boolean isGroupMatch = match.getStageId().equals(Stage.GROUP);
            if (isGroupMatch) {
                // check userBet group and if there is knockout userBer
                GroupMatch groupMatch = (GroupMatch) match;
                Group groupId = groupMatch.getGroupId();
            } else {
                KnockoutMatch knockoutMatch = (KnockoutMatch) match;
                knockoutMatch.getAwayTeamCode();
                knockoutMatch.getHomeTeamCode();
            }

//            List<UserBet> nextStageBets = betService.findByStageId(match.getStageId().getNextStage());

//            if (CollectionUtils.isNotEmpty(nextStageBets)) {
//                errors.rejectValue("betId", "", "There are bets for next stage, delete them to modify current");
//            }
        }
    }
}
