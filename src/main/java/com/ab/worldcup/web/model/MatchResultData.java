package com.ab.worldcup.web.model;

import com.ab.worldcup.knockout.KnockoutMatchQualifier;
import lombok.Data;

@Data
public class MatchResultData {

    private Long matchId;

    private String homeTeamCode;

    private String awayTeamCode;

    private Integer homeTeamGoals;

    private Integer awayTeamGoals;

    private KnockoutMatchQualifier matchQualifier;
}
