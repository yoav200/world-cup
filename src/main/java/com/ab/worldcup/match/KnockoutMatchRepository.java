package com.ab.worldcup.match;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnockoutMatchRepository extends JpaRepository<KnockoutMatch, Long> {

    KnockoutMatch findByMatchCode(KnockoutMatchCode matchCode);

    List<KnockoutMatch> findAllByStageId(Stage stageId);

}
