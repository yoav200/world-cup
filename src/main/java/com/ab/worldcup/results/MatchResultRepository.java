package com.ab.worldcup.results;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {

    @Query(value = "SELECT r.* FROM match_result r, group_match m  WHERE m.matchId = r.matchId and m.groupId=?1", nativeQuery = true)
    List<MatchResult> findMatchResultByGroup(String groupId);

}
