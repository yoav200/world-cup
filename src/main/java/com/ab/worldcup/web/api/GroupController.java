package com.ab.worldcup.web.api;

import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.group.GroupStanding;
import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.results.ResultsService;
import com.ab.worldcup.team.Group;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/groups")
public class GroupController {

  private final GroupService groupService;

  private final ResultsService resultsService;

  @RequestMapping("/")
  public List<GroupStanding> getAllGroupStanding() {
    List<GroupStanding> list = new ArrayList<>();
    for (Group groupId : Group.values()) {
      List<MatchResult> matchResultList = resultsService.getMatchResultForGroup(groupId);
      GroupStanding groupStanding = groupService.getGroupStanding(groupId, matchResultList);
      list.add(groupStanding);
    }
    return list;
  }

  @RequestMapping("/{groupId}")
  public GroupStanding getGroupStanding(@PathVariable Group groupId) {
    List<MatchResult> matchResultList = resultsService.getMatchResultForGroup(groupId);
    return groupService.getGroupStanding(groupId, matchResultList);
  }

}
