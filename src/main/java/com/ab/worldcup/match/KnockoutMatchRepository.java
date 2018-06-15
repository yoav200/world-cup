package com.ab.worldcup.match;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KnockoutMatchRepository extends JpaRepository<KnockoutMatch, Long> {

    KnockoutMatch findByMatchCode(KnockoutMatchCode matchCode);

    List<KnockoutMatch> findAllByStageId(Stage stageId);

}
