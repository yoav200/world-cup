package com.ab.worldcup.group;

import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.results.MatchResultType;
import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.team.Team;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Getter
public class TeamInGroup implements Comparable<TeamInGroup> {

    private Set<GroupMatch> matches;
    private Set<ResultInterface> results;
    private Team team;

    private int gamesPlayed;
    private int gamesWon;
    private int gamesTied;
    private int gamesLose;
    private int goalsScored;
    private int goalsReceived;
    private int points;

    public TeamInGroup(Team team, List<GroupMatch> matches){
        this.team = team;
        this.matches = matches.stream().collect(Collectors.toSet());
        this.results = new TreeSet<>();
    }

    public TeamInGroup addMatchResults(List<ResultInterface> resultList){
        for (ResultInterface result : resultList) {
            // Checking that these games wasn't add already
            if(!results.stream().map(t -> t.getMatchId()).collect(Collectors.toList()).contains(result.getMatchId())) {
                boolean isTeamHomeTeam = result.getHomeTeam().equals(team);
                boolean isTeamAwayTeam = result.getAwayTeam().equals(team);

                if (isTeamHomeTeam || isTeamAwayTeam) {
                    gamesPlayed++;
                    goalsScored += isTeamAwayTeam ? result.getHomeTeamGoals() : result.getAwayTeamGoals();
                    goalsReceived += isTeamAwayTeam ? result.getAwayTeamGoals() : result.getHomeTeamGoals();

                    switch (result.getWinner()) {
                        case HOME_TEAM_WON:
                            if (isTeamHomeTeam) {
                                gamesWon++;
                                points += 3;
                            } else {
                                gamesLose++;
                            }
                            break;
                        case AWAY_TEAM_WON:
                            if (isTeamHomeTeam) {
                                gamesLose++;
                            } else {
                                gamesWon++;
                                points += 3;
                            }
                            break;
                        case DRAW:
                            gamesTied++;
                            points += 1;
                    }
                }
            }
        }
        return this;
    }

    @Override
    public int compareTo(TeamInGroup that) {
        // FIFA TIE BREAKING POLICY
        // https://en.wikipedia.org/wiki/2018_FIFA_World_Cup_qualification#Tiebreakers

        // 1. Points
        if (this.points != that.points) {
            return this.points - that.points;
        } // 2. Goal Diff
        else if ((this.goalsScored - this.goalsReceived) != (that.goalsScored - that.goalsReceived)) {
            return (this.goalsScored - this.goalsReceived) - (that.goalsScored - that.goalsReceived);
        } // 3. Goals Scored
        else if (this.goalsScored != that.goalsScored) {
            return this.goalsScored - that.goalsScored;
        } // 4. result in direct match between two teams
        else{

            List<ResultInterface> directMatches = this.getResults().stream().
                    filter(match -> (match.getHomeTeam().equals(this.team) && match.getAwayTeam().equals(that.team)) ||
                            (match.getAwayTeam().equals(this.team) && match.getHomeTeam().equals(that.team))).collect(Collectors.toList());
            if(!directMatches.isEmpty()){
                // Assuming only one direct match
                ResultInterface directMatchResult = directMatches.get(0);
                if(!directMatchResult.getWinner().equals(MatchResultType.DRAW)){

                    if ((directMatchResult.getHomeTeam().equals(this.team) && directMatchResult.getWinner().equals(MatchResultType.HOME_TEAM_WON)) ||
                        (directMatchResult.getAwayTeam().equals(this.team) && directMatchResult.getWinner().equals(MatchResultType.AWAY_TEAM_WON))){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            }
            // DirectMatch does not Exists or ended up with tie
            // Go to configuration file
            return TieBreakConfig.getTeamRank(this.team.getId()) - TieBreakConfig.getTeamRank(that.team.getId());
        }

    }
}
