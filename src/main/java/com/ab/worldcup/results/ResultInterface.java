package com.ab.worldcup.results;

import com.ab.worldcup.team.Team;

public interface ResultInterface {

    Long getMatchId();

    Team getHomeTeam();

    Team getAwayTeam();

    Integer getHomeTeamGoals();

    Integer getAwayTeamGoals();

    Team getKnockoutQualifier();

    default MatchResultType getWinner() {
        MatchResultType winner = MatchResultType.DRAW;
        if(getMatchId() != null) {
            if (getHomeTeamGoals() > getAwayTeamGoals()) {
                winner = MatchResultType.HOME_TEAM_WON;
            } else if (getHomeTeamGoals() < getAwayTeamGoals()) {
                winner = MatchResultType.AWAY_TEAM_WON;
            }
        }
        return winner;
    }

    default boolean winnerEquals(ResultInterface other) {
        if (getMatchId().equals(other.getMatchId())) {
            if (getHomeTeam().equals(other.getHomeTeam())
                    && getAwayTeam().equals(other.getAwayTeam())
                    && getWinner().equals(other.getWinner())) {
                return true;
            } else if (getHomeTeam().equals(other.getAwayTeam())
                    && getAwayTeam().equals(other.getHomeTeam())
                    && getWinner().opposite().equals(other.getWinner())) {
                return true;
            }

        }
        return false;
    }

    default boolean scoreEquals(ResultInterface other) {
        if (getMatchId().equals(other.getMatchId())) {
            if (getHomeTeam().equals(other.getHomeTeam())
                    && getAwayTeam().equals(other.getAwayTeam())
                    && getHomeTeamGoals() == other.getHomeTeamGoals()
                    && getAwayTeamGoals() == other.getAwayTeamGoals()) {
                return true;
            } else if (getHomeTeam().equals(other.getAwayTeam())
                    && getAwayTeam().equals(other.getHomeTeam())
                    && getHomeTeamGoals() == other.getAwayTeamGoals()
                    && getAwayTeamGoals() == other.getHomeTeamGoals()) {
                return true;
            }

        }
        return false;
    }
}
