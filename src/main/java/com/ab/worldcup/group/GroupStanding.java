package com.ab.worldcup.group;

import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.Team;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Getter
public class GroupStanding {

    @JsonProperty(value = "groupId")
    private Group groupId;

    @JsonProperty(value = "teamsInGroup")
    private Set<TeamInGroup<? super ResultInterface>> teamsInGroup;

    <T extends ResultInterface> GroupStanding(Group group, List<GroupMatch> matches, List<T> results, List<Team> teams) {
        this.groupId = group;
        teamsInGroup = new TreeSet<>(TeamInGroup.comparator);
        for (Team team : teams) {
            TeamInGroup<? super ResultInterface> teamInGroup = new TeamInGroup(team, matches).addMatchResults(results);
            teamsInGroup.add(teamInGroup);
        }
    }

    @JsonIgnore
    public List<TeamInGroup> getStanding() {
        return teamsInGroup.stream().sorted().collect(Collectors.toList());
    }
}
