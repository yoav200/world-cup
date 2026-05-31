package com.ab.worldcup.coins;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class CoinService {

  private final CoinEventRepository coinEventRepository;
  private final CoinConfig coinConfig;

  public void emitLoginEvent(Long accountId) {
    LocalDate today = LocalDate.now();
    LocalDateTime startOfDay = today.atStartOfDay();
    LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

    if (coinEventRepository.existsByEventTypeAndAccountIdAndCreatedAtBetween(
        CoinEventType.LOGIN, accountId, startOfDay, endOfDay)) {
      return;
    }

    CoinEvent event = CoinEvent.builder()
        .eventType(CoinEventType.LOGIN)
        .coins(coinConfig.getRateForEvent(CoinEventType.LOGIN))
        .accountId(accountId)
        .build();
    coinEventRepository.save(event);
    log.info("Coin event LOGIN emitted for account {}", accountId);
  }

  public void emitBetPlacedEvent(Long accountId, Long betId) {
    if (coinEventRepository.existsByEventTypeAndAccountIdAndReferenceId(
        CoinEventType.BET_PLACED, accountId, betId)) {
      return;
    }

    CoinEvent event = CoinEvent.builder()
        .eventType(CoinEventType.BET_PLACED)
        .coins(coinConfig.getRateForEvent(CoinEventType.BET_PLACED))
        .accountId(accountId)
        .referenceId(betId)
        .build();
    coinEventRepository.save(event);
    log.info("Coin event BET_PLACED emitted for account {}, bet {}", accountId, betId);
  }

  public void emitQualifierBetPlacedEvent(Long accountId) {
    if (coinEventRepository.existsByEventTypeAndAccountIdAndReferenceId(
        CoinEventType.QUALIFIER_BET_PLACED, accountId, null)) {
      return;
    }

    CoinEvent event = CoinEvent.builder()
        .eventType(CoinEventType.QUALIFIER_BET_PLACED)
        .coins(coinConfig.getRateForEvent(CoinEventType.QUALIFIER_BET_PLACED))
        .accountId(accountId)
        .build();
    coinEventRepository.save(event);
    log.info("Coin event QUALIFIER_BET_PLACED emitted for account {}", accountId);
  }

  public void emitFullRoundEvent(Long accountId, Long referenceId) {
    if (coinEventRepository.existsByEventTypeAndAccountIdAndReferenceId(
        CoinEventType.FULL_ROUND, accountId, referenceId)) {
      return;
    }

    CoinEvent event = CoinEvent.builder()
        .eventType(CoinEventType.FULL_ROUND)
        .coins(coinConfig.getRateForEvent(CoinEventType.FULL_ROUND))
        .accountId(accountId)
        .referenceId(referenceId)
        .build();
    coinEventRepository.save(event);
    log.info("Coin event FULL_ROUND emitted for account {}, reference {}", accountId, referenceId);
  }

  public void emitGlobalEvent(CoinEventType type) {
    if (type != CoinEventType.START_SEED && type != CoinEventType.SECOND_ROUND_BONUS && type != CoinEventType.END_BONUS) {
      throw new IllegalArgumentException("Only global event types allowed: START_SEED, SECOND_ROUND_BONUS, END_BONUS");
    }
    if (coinEventRepository.existsByEventType(type)) {
      log.debug("Global event {} already emitted, skipping", type);
      return;
    }

    CoinEvent event = CoinEvent.builder()
        .eventType(type)
        .coins(coinConfig.getRateForEvent(type))
        .build();
    coinEventRepository.save(event);
    log.info("Global coin event {} emitted with {} coins", type, event.getCoins());
  }

  public long getBucketTotal() {
    return coinEventRepository.sumAllCoins();
  }

  public long getBucketTotalForAccounts(Set<Long> accountIds) {
    if (accountIds == null || accountIds.isEmpty()) {
      return 0;
    }
    return coinEventRepository.sumCoinsByAccountIds(accountIds);
  }

  public long getGlobalEventsTotal() {
    return coinEventRepository.sumGlobalCoins();
  }

  public Map<CoinEventType, BucketBreakdown> getBucketBreakdown() {
    return coinEventRepository.getBreakdownByType().stream()
        .collect(Collectors.toMap(
            row -> (CoinEventType) row[0],
            row -> new BucketBreakdown((Long) row[1], (Long) row[2])
        ));
  }

  public Map<Integer, Integer> getDistribution() {
    return coinConfig.getDistribution();
  }

  public List<CoinEvent> getRecentEvents() {
    return coinEventRepository.findAllByOrderByCreatedAtDesc();
  }

  public record BucketBreakdown(long count, long totalCoins) {}
}
