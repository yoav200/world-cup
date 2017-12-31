package com.ab.worldcup.match;

import com.ab.worldcup.team.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMatchRepository extends JpaRepository<GroupMatch, Long> {

    List<GroupMatch> findByGroupId(Group group);
}
