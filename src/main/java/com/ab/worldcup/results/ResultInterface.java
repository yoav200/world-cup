package com.ab.worldcup.results;

import com.ab.worldcup.team.Team;

public interface ResultInterface {
    Long getMatchId();
    Team getHomeTeam();
    Team getAwayTeam();
    int getHomeTeamGoals();
    int getAwayTeamGoals();
    MatchResultType getWinner();
}
