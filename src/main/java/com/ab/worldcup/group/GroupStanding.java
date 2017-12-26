package com.ab.worldcup.group;

import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.Team;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class GroupStanding {

    Group groupId;
    Set<TeamInGroup> teamsInGroup;

    public  <T extends ResultInterface> GroupStanding(Group group, List<GroupMatch> matches, List<T> results, List<Team> teams){
        this.groupId = group;
        teamsInGroup = new TreeSet<>();
        for (Team team : teams) {
            TeamInGroup teamInGroup = new TeamInGroup(team, matches);
            teamInGroup.addMatchResults(results);
            teamsInGroup.add(teamInGroup);
        }
    }

    public List<TeamInGroup> getStanding(){
        return teamsInGroup.stream().sorted().collect(Collectors.toList());
    }
}
