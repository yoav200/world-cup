package com.ab.worldcup.web.model;

import com.ab.worldcup.knockout.KnockoutMatchQualifier;
import com.ab.worldcup.match.Stage;
import lombok.Data;
import org.springframework.validation.Errors;

@Data
public class MatchResultData {

    private Long matchId;

    private String homeTeamCode;

    private String awayTeamCode;

    private Integer homeTeamGoals;

    private Integer awayTeamGoals;

    private KnockoutMatchQualifier matchQualifier;

    public void validate(Errors errors, Stage stage) {
        if ((getHomeTeamGoals() == null || getHomeTeamGoals() < 0)) {
            errors.rejectValue("homeTeamGoals", "", "Home team goals must be set (0 or more)");
        }

        if ((getAwayTeamGoals() == null || getAwayTeamGoals() < 0)) {
            errors.rejectValue("awayTeamGoals", "", "Away team goals must be set (0 or more)");
        }

        /*
        if (!Stage.GROUP.equals(stage)) {
            if (getMatchQualifier() == null) {
                errors.rejectValue("matchQualifier", "", "Missing qualifier for knockout match");
            }
        }
        */
    }
}
