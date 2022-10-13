package com.ab.worldcup.match;

import com.ab.worldcup.team.Group;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMatchRepository extends JpaRepository<GroupMatch, Long> {

  List<GroupMatch> findByGroupId(Group group);
}
