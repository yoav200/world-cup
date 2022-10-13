package com.ab.worldcup.group;

import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.match.GroupMatchRepository;
import com.ab.worldcup.match.Match;
import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.Team;
import com.ab.worldcup.team.TeamRepository;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GroupService {

  private final GroupMatchRepository groupMatchRepository;

  private final TeamRepository teamRepository;

  public <T extends ResultInterface> boolean isGroupFinished(Group groupId, List<T> matches) {
    List<GroupMatch> groupMatches = groupMatchRepository.findByGroupId(groupId);
    List<Long> matchesInGroup = groupMatches.stream().map(Match::getMatchId).collect(Collectors.toList());
    return new HashSet<>(matches.stream().map(ResultInterface::getMatchId).collect(Collectors.toList())).containsAll(
        matchesInGroup);
  }

  public <T extends ResultInterface> GroupStanding getGroupStanding(Group groupId, List<T> results) {
    List<GroupMatch> groupMatches = groupMatchRepository.findByGroupId(groupId);
    List<Team> teams = teamRepository.findByGroupId(groupId);
    return new GroupStanding(groupId, groupMatches, results, teams);
  }

  public Group getGroupIdByMatchId(Long matchId) {
    GroupMatch groupMatch = groupMatchRepository.findById(matchId).orElseThrow();
    return groupMatch.getGroupId();
  }

  public Team findTeamByCode(String code) {
    return teamRepository.findByCode(code);
  }

  public List<Team> getTeamsByGroup(Group groupId) {
    return teamRepository.findByGroupId(groupId);
  }

  @Cacheable("allGroupMatchesCache")
  public List<GroupMatch> getAllGroupMatches() {
    return groupMatchRepository.findAll();
  }
}
