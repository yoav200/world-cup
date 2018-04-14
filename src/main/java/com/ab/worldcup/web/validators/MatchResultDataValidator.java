package com.ab.worldcup.web.validators;

import com.ab.worldcup.match.Match;
import com.ab.worldcup.match.MatchService;
import com.ab.worldcup.web.model.MatchResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("MatchResultDataValidator")
public class MatchResultDataValidator implements Validator {

    private final MatchService matchService;

    @Autowired
    public MatchResultDataValidator(MatchService matchService) {
        this.matchService = matchService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MatchResultData.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MatchResultData matchResultData = (MatchResultData) target;

        Match match = matchService.getMatchById(matchResultData.getMatchId());

        if (match == null) {
            errors.rejectValue("matchId", "", "Match not found");
        } else {
            matchResultData.validate(errors, match.getStageId());
        }
    }
}
