package com.ab.worldcup.league;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<League, Long> {

  List<League> findByCreatedById(Long accountId);

  long countByCreatedById(Long accountId);
}
