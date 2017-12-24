package com.ab.worldcup.group;

import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.match.GroupMatchRepository;
import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.Team;
import com.ab.worldcup.team.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    GroupMatchRepository groupMatchRepository;

    @Autowired
    TeamRepository teamRepository;

    public boolean isGroupFinished(Group groupId,List<ResultInterface> matches){
        List<GroupMatch> groupMatches = groupMatchRepository.findByGroupId(groupId);
        List<Long> matchesInGroup = groupMatches.stream().map(t -> t.getMatchId()).collect(Collectors.toList());
        return matches.stream().map(t-> t.getMatchId()).collect(Collectors.toList()).containsAll(matchesInGroup);
    }

    public List<TeamInGroup> getGroupStanding(Group groupId, List<ResultInterface> results){
        List<GroupMatch> groupMatches = groupMatchRepository.findByGroupId(groupId);
        List<Team> teams = teamRepository.findByGroupId(groupId);
        GroupStanding groupStanding = new GroupStanding(groupId,groupMatches,results,teams);
        return groupStanding.getStanding();
    }
}
