package com.ab.worldcup.coins;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoinEventRepository extends JpaRepository<CoinEvent, Long> {

  boolean existsByEventType(CoinEventType eventType);

  boolean existsByEventTypeAndAccountIdAndCreatedAtBetween(
      CoinEventType eventType, Long accountId, LocalDateTime from, LocalDateTime to);

  boolean existsByEventTypeAndAccountIdAndReferenceId(
      CoinEventType eventType, Long accountId, Long referenceId);

  @Query("SELECT COALESCE(SUM(e.coins), 0) FROM CoinEvent e")
  long sumAllCoins();

  @Query("SELECT COALESCE(SUM(e.coins), 0) FROM CoinEvent e WHERE e.accountId IN :accountIds")
  long sumCoinsByAccountIds(Set<Long> accountIds);

  @Query("SELECT COALESCE(SUM(e.coins), 0) FROM CoinEvent e WHERE e.accountId IS NULL")
  long sumGlobalCoins();

  List<CoinEvent> findAllByOrderByCreatedAtDesc();

  @Query("SELECT e.eventType, COUNT(e), COALESCE(SUM(e.coins), 0) FROM CoinEvent e GROUP BY e.eventType")
  List<Object[]> getBreakdownByType();
}
