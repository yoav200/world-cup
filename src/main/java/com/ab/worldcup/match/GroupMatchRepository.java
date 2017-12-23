package com.ab.worldcup.match;

import com.ab.worldcup.team.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GroupMatchRepository extends JpaRepository<GroupMatch, Long> {

    List<GroupMatch> findByGroupId(Group group);
}
