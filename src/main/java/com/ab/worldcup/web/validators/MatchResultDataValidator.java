package com.ab.worldcup.web.validators;

import com.ab.worldcup.match.Match;
import com.ab.worldcup.match.MatchService;
import com.ab.worldcup.match.Stage;
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
            if ((matchResultData.getHomeTeamGoals() == null || matchResultData.getHomeTeamGoals() < 0)) {
                errors.rejectValue("homeTeamGoals", "", "Home team goals result must be set");
            }

            if ((matchResultData.getAwayTeamGoals() == null || matchResultData.getAwayTeamGoals() < 0)) {
                errors.rejectValue("awayTeamGoals", "", "Away team goals result must be set");
            }

            if (!Stage.GROUP.equals(match.getStageId())) {
                if (matchResultData.getMatchQualifier() == null) {
                    errors.rejectValue("matchQualifier", "", "Missing qualifier for knockout match");
                }
            }
        }
    }
}
