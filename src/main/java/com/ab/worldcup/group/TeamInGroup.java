package com.ab.worldcup.group;

import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.results.MatchResultType;
import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.team.Team;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class TeamInGroup<T extends ResultInterface> implements Comparable<TeamInGroup> {

    @JsonProperty("team")
    private Team team;

    @JsonProperty("matches")
    private Set<GroupMatch> matches;

    @JsonIgnore
    private Set<T> results;

    private int gamesPlayed = 0;
    private int gamesWon = 0;
    private int gamesTied = 0;
    private int gamesLose = 0;
    private int goalsScored = 0;
    private int goalsReceived = 0;
    private int points = 0;

    TeamInGroup(Team team, List<GroupMatch> matches) {
        this.team = team;
        this.matches = matches.stream().filter(m -> (m.getAwayTeam().equals(team) || m.getHomeTeam().equals(team))).collect(Collectors.toSet());
        this.results = new TreeSet<>();
    }

    TeamInGroup addMatchResults(List<T> resultList) {
        Map<Long, GroupMatch> groupMatchMap = matches.stream().collect(Collectors.toMap(GroupMatch::getMatchId, Function.identity()));
        List<Long> matchIds = results.stream().map(ResultInterface::getMatchId).collect(Collectors.toList());

        for (ResultInterface result : resultList) {
            // Checking that these games wasn't add already
            if (!matchIds.contains(result.getMatchId()) && groupMatchMap.containsKey(result.getMatchId())) {
                GroupMatch groupMatch = groupMatchMap.get(result.getMatchId());
                boolean isTeamHomeTeam = groupMatch.getHomeTeam().equals(team);
                boolean isTeamAwayTeam = groupMatch.getAwayTeam().equals(team);

                if (isTeamHomeTeam || isTeamAwayTeam) {
                    gamesPlayed++;
                    goalsScored += isTeamHomeTeam ? result.getHomeTeamGoals() : result.getAwayTeamGoals();
                    goalsReceived += isTeamHomeTeam ? result.getAwayTeamGoals() : result.getHomeTeamGoals();

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
                    groupMatchMap.computeIfPresent(result.getMatchId(), (k, v) -> {
                        v.setResult(result);
                        return v;
                    });
                }
            }
        }
        return this;
    }

    public static Comparator<TeamInGroup<? super ResultInterface>> comparator = (t2, t1) -> {
        // FIFA TIE BREAKING POLICY
        // https://en.wikipedia.org/wiki/2018_FIFA_World_Cup_qualification#Tiebreakers

        // 1. Points
        if (t1.points != t2.points) {
            return t1.points - t2.points;
        } // 2. Goal Diff
        else if ((t1.goalsScored - t1.goalsReceived) != (t2.goalsScored - t2.goalsReceived)) {
            return (t1.goalsScored - t1.goalsReceived) - (t2.goalsScored - t2.goalsReceived);
        } // 3. Goals Scored
        else if (t1.goalsScored != t2.goalsScored) {
            return t1.goalsScored - t2.goalsScored;
        } // 4. result in direct match between two teams
        else {
            List<ResultInterface> directMatches = t1.getResults()
                    .stream()
                    .filter(match ->
                            (match.getHomeTeam().equals(t1.team) && match.getAwayTeam().equals(t2.team)) ||
                                    (match.getAwayTeam().equals(t1.team) && match.getHomeTeam().equals(t2.team)))
                    .collect(Collectors.toList());
            if (!directMatches.isEmpty()) {
                // Assuming only t1 direct match
                ResultInterface directMatchResult = directMatches.get(0);
                if (!directMatchResult.getWinner().equals(MatchResultType.DRAW)) {
                    if ((directMatchResult.getHomeTeam().equals(t1.team) && directMatchResult.getWinner().equals(MatchResultType.HOME_TEAM_WON)) ||
                            (directMatchResult.getAwayTeam().equals(t1.team) && directMatchResult.getWinner().equals(MatchResultType.AWAY_TEAM_WON))) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
            // DirectMatch does not Exists or ended up with tie
            // Go to configuration file
            return t1.team.getFifaRanking().compareTo(t2.team.getFifaRanking());
        }
    };


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
        else {
            List<ResultInterface> directMatches = this.getResults()
                    .stream()
                    .filter(match ->
                            (match.getHomeTeam().equals(this.team) && match.getAwayTeam().equals(that.team)) ||
                                    (match.getAwayTeam().equals(this.team) && match.getHomeTeam().equals(that.team)))
                    .collect(Collectors.toList());
            if (!directMatches.isEmpty()) {
                // Assuming only one direct match
                ResultInterface directMatchResult = directMatches.get(0);
                if (!directMatchResult.getWinner().equals(MatchResultType.DRAW)) {

                    if ((directMatchResult.getHomeTeam().equals(this.team) && directMatchResult.getWinner().equals(MatchResultType.HOME_TEAM_WON)) ||
                            (directMatchResult.getAwayTeam().equals(this.team) && directMatchResult.getWinner().equals(MatchResultType.AWAY_TEAM_WON))) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
            // DirectMatch does not Exists or ended up with tie
            // Go to configuration file
            return team.getFifaRanking().compareTo(that.team.getFifaRanking());
            //return TieBreakConfig.getTeamRank(this.team.getId()) - TieBreakConfig.getTeamRank(that.team.getId());
        }
    }
}
