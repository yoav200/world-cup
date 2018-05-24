package com.ab.worldcup.match;

import com.ab.worldcup.team.KnockoutTeamCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface KnockoutMatchRepository extends JpaRepository<KnockoutMatch, Long> {

    KnockoutMatch findByMatchCode(KnockoutMatchCode matchCode);

    List<KnockoutMatch> findAllByStageId(Stage stageId);

//    List<KnockoutMatch> findAllByHomeTeamCodeInOrAwayTeamCodeIn(Set<KnockoutTeamCode> codes1, Set<KnockoutTeamCode> codes2);
}
