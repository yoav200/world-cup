package com.ab.worldcup.knockout;

import com.ab.worldcup.team.Team;

public interface KnockoutTeamInterface {

    Long getMatchId();

    Team getHomeTeam();

    Team getAwayTeam();

}
