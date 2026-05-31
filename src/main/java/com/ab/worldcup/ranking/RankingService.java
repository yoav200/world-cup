package com.ab.worldcup.ranking;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountRepository;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.bet.UserBet;
import com.ab.worldcup.results.CalculatedUserBet;
import com.ab.worldcup.results.ResultsService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@AllArgsConstructor
public class RankingService {

  private final ResultsService resultsService;

  private final BetService betService;

  private final AccountRepository accountRepository;

  private final RankingRepository rankingRepository;

  // ── Leaderboard (read path) — always live ───────────────────────────

  public List<RankingDataNew> getLeaderboard() {
    List<Ranking> current = computeLiveRanking();

    // Load the latest snapshot for rank-movement comparison
    List<Ranking> previous = getLatestSnapshot();

    return compareRanking(current, previous);
  }

  // ── Write path — snapshot for movement tracking ─────────────────────

  /**
   * Called after a match result is saved.
   * Computes the live ranking and saves a snapshot so the next call
   * to getLeaderboard() can show rank movement arrows.
   * Keeps only the 2 most recent snapshots.
   */
  @Async
  @Transactional
  public void onMatchResultUpdated(Long matchId) {
    List<Ranking> liveRanking = computeLiveRanking();
    Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    liveRanking.forEach(r -> r.setDate(now));
    rankingRepository.saveAll(liveRanking);
    log.info("Saved ranking snapshot with {} entries for matchId={}", liveRanking.size(), matchId);

    pruneOldSnapshots();
  }

  // ── Live computation ────────────────────────────────────────────────

  private List<Ranking> computeLiveRanking() {
    List<Account> activeUsers = accountRepository.findAllActiveUsers();
    List<Ranking> rankings = new ArrayList<>(activeUsers.size());

    for (Account account : activeUsers) {
      List<UserBet> bets = betService.findByUserBetIdAccountId(account.getId());
      List<CalculatedUserBet> calculated = resultsService.calculateBetsForUser(bets);
      int totalPoints = calculated.stream().mapToInt(CalculatedUserBet::getTotalPoints).sum();

      rankings.add(Ranking.builder()
          .account(account)
          .points(totalPoints)
          .build());
    }
    return rankings;
  }

  // ── Snapshot helpers ────────────────────────────────────────────────

  private List<Ranking> getLatestSnapshot() {
    List<Timestamp> dates = rankingRepository.findDistinctDates();
    if (dates != null && !dates.isEmpty()) {
      return rankingRepository.findAllByDate(dates.getFirst());
    }
    return List.of();
  }

  private void pruneOldSnapshots() {
    List<Timestamp> dates = rankingRepository.findDistinctDates();
    if (dates != null && dates.size() > 2) {
      // Keep only the 2 most recent snapshots, delete the rest
      List<Timestamp> toDelete = dates.subList(2, dates.size());
      for (Timestamp old : toDelete) {
        rankingRepository.deleteByDate(old);
      }
      log.info("Pruned {} old ranking snapshots", toDelete.size());
    }
  }

  // ── Ranking comparison ────────────────────────────────────────────────

  private List<RankingDataNew> convertRanking(List<Ranking> rankings) {
    List<RankingDataNew> rankingData = new ArrayList<>(rankings.size());
    rankings.sort(Comparator.comparing(Ranking::getPoints).reversed());
    Integer rankPos = 1;
    Integer lastPoints = null;
    for (Ranking ranking : rankings) {
      Integer currentPoint = ranking.getPoints();
      if (lastPoints != null && !lastPoints.equals(currentPoint)) {
        rankPos++;
      }
      lastPoints = currentPoint;

      rankingData.add(RankingDataNew.builder()
          .account(ranking.getAccount())
          .date(ranking.getDate())
          .points(ranking.getPoints())
          .rank(rankPos).build());
    }
    return rankingData;
  }

  private List<RankingDataNew> compareRanking(List<Ranking> current, List<Ranking> previous) {
    List<RankingDataNew> currentRankingData = convertRanking(current);
    if (!previous.isEmpty()) {
      List<RankingDataNew> prevRankingData = convertRanking(previous);
      Map<Long, RankingDataNew> prevRankLookup = prevRankingData.stream()
          .collect(Collectors.toMap(o -> o.getAccount().getId(), Function.identity()));
      currentRankingData.forEach(rankingDataNew -> {
        RankingDataNew prevData = prevRankLookup.get(rankingDataNew.getAccount().getId());
        if (prevData != null) {
          rankingDataNew.setPrevPoints(prevData.getPoints());
          rankingDataNew.setPrevRank(prevData.getRank());
        }
      });
    }
    return currentRankingData;
  }
}
