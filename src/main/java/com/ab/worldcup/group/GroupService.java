package com.ab.worldcup.group;

import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.match.GroupMatchRepository;
import com.ab.worldcup.match.Match;
import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.results.MatchResultRepository;
import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.Team;
import com.ab.worldcup.team.TeamRepository;
import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupMatchRepository groupMatchRepository;

    @Autowired
    private MatchResultRepository matchResultRepository;

    @Autowired
    private TeamRepository teamRepository;

    public <T extends ResultInterface> boolean isGroupFinished(Group groupId, List<T> matches) {
        List<GroupMatch> groupMatches = groupMatchRepository.findByGroupId(groupId);
        List<Long> matchesInGroup = groupMatches.stream().map(Match::getMatchId).collect(Collectors.toList());
        return matches.stream().map(ResultInterface::getMatchId).collect(Collectors.toList()).containsAll(matchesInGroup);
    }

    public <T extends ResultInterface> GroupStanding getGroupStanding(Group groupId, List<T> results) {
        List<GroupMatch> groupMatches = groupMatchRepository.findByGroupId(groupId);
        List<Team> teams = teamRepository.findByGroupId(groupId);
        return new GroupStanding(groupId, groupMatches, results, teams);
    }

    public Group getGroupIdByMatchId(Long matchId) {
        GroupMatch groupMatch = groupMatchRepository.findOne(matchId);
        return groupMatch.getGroupId();
    }

    public Set<GroupMatch> getAllGroupMatchs() {
        List<MatchResult> results = matchResultRepository.findAll();
        List<GroupMatch> matches = groupMatchRepository.findAll();
        Map<Long, MatchResult> resultMap = results.stream().collect(Collectors.toMap(MatchResult::getMatchId, Function.identity()));

        matches.forEach(match -> resultMap.computeIfPresent(match.getMatchId(), (k, v) -> {
            match.setResult(v);
            return v;
        }));
        return ImmutableSet.copyOf(matches);
    }
}
