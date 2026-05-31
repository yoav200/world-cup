package com.ab.worldcup.league;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LeagueMembershipRepository extends JpaRepository<LeagueMembership, Long> {

  List<LeagueMembership> findByLeagueId(Long leagueId);

  Optional<LeagueMembership> findByLeagueIdAndEmail(Long leagueId, String email);

  @Query("SELECT m FROM LeagueMembership m WHERE m.account.id = :accountId")
  List<LeagueMembership> findByAccountId(Long accountId);

  @Query("SELECT m FROM LeagueMembership m WHERE m.email = :email AND m.account IS NULL")
  List<LeagueMembership> findUnlinkedByEmail(String email);

  @Query("SELECT m.account.id FROM LeagueMembership m WHERE m.league.id = :leagueId AND m.account IS NOT NULL")
  List<Long> findLinkedAccountIdsByLeagueId(Long leagueId);
}
