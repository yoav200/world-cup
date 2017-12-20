package com.ab.worldcup.match;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KnockoutMatchRepository extends JpaRepository<KnockoutMatch, Long> {
    KnockoutMatch findByKnockoutMatchCode(KnockoutMatchCode matchCode);
}
