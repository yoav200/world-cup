package com.ab.worldcup.ranking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountRepository;
import com.ab.worldcup.account.AccountStatus;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.results.CalculatedUserBet;
import com.ab.worldcup.results.ResultsService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

  @Mock private ResultsService resultsService;
  @Mock private BetService betService;
  @Mock private AccountRepository accountRepository;
  @Mock private RankingRepository rankingRepository;

  @InjectMocks private RankingService rankingService;

  private Account alice;
  private Account bob;
  private Account charlie;

  @BeforeEach
  void setUp() {
    alice = Account.builder().id(1L).email("alice@test.com")
        .firstName("Alice").lastName("A").enabled(true).status(AccountStatus.ACTIVE).build();
    bob = Account.builder().id(2L).email("bob@test.com")
        .firstName("Bob").lastName("B").enabled(true).status(AccountStatus.ACTIVE).build();
    charlie = Account.builder().id(3L).email("charlie@test.com")
        .firstName("Charlie").lastName("C").enabled(true).status(AccountStatus.ACTIVE).build();
  }

  // ── Helpers ─────────────────────────────────────────────────────────────

  private Ranking ranking(Account account, int points, Timestamp date) {
    return Ranking.builder().account(account).points(points).date(date).build();
  }

  // ── getLeaderboard tests ────────────────────────────────────────────────

  @Nested
  @DisplayName("getLeaderboard")
  class GetLeaderboard {

    @Test
    @DisplayName("no bets placed → all users at 0 points, rank 1")
    void noBets_allZero() {
      when(accountRepository.findAllActiveUsers()).thenReturn(List.of(alice, bob));
      when(betService.findByUserBetIdAccountId(anyLong())).thenReturn(List.of());
      when(resultsService.calculateBetsForUser(anyList())).thenReturn(List.of());
      when(rankingRepository.findDistinctDates()).thenReturn(List.of());

      List<RankingDataNew> result = rankingService.getLeaderboard();

      assertThat(result).hasSize(2);
      assertThat(result).allMatch(r -> r.getPoints() == 0);
      assertThat(result).allMatch(r -> r.getRank() == 1);
      assertThat(result).allMatch(r -> r.getPrevRank() == null);
      assertThat(result).allMatch(r -> r.getPrevPoints() == null);
    }

    @Test
    @DisplayName("live ranking with snapshot → prevRank from snapshot")
    void liveWithSnapshot() {
      Timestamp t1 = Timestamp.valueOf(LocalDateTime.of(2026, 6, 15, 12, 0));

      // Live: Alice=15, Bob=20, Charlie=10
      when(accountRepository.findAllActiveUsers()).thenReturn(List.of(alice, bob, charlie));
      when(betService.findByUserBetIdAccountId(anyLong())).thenReturn(List.of());
      when(resultsService.calculateBetsForUser(anyList())).thenAnswer(inv -> {
        // Called 3 times in order: alice, bob, charlie
        return List.of();
      });
      // We need per-account point stubs — use answer based on invocation order
      when(betService.findByUserBetIdAccountId(1L)).thenReturn(List.of()); // alice
      when(betService.findByUserBetIdAccountId(2L)).thenReturn(List.of()); // bob
      when(betService.findByUserBetIdAccountId(3L)).thenReturn(List.of()); // charlie

      // Mock calculateBetsForUser to return different totals per call
      var aliceCalc = CalculatedUserBet.builder().matchResultPoints(15).exactScorePoints(0).correctQualifierPoints(0).build();
      var bobCalc = CalculatedUserBet.builder().matchResultPoints(20).exactScorePoints(0).correctQualifierPoints(0).build();
      var charlieCalc = CalculatedUserBet.builder().matchResultPoints(10).exactScorePoints(0).correctQualifierPoints(0).build();
      when(resultsService.calculateBetsForUser(anyList()))
          .thenReturn(List.of(aliceCalc))   // alice
          .thenReturn(List.of(bobCalc))     // bob
          .thenReturn(List.of(charlieCalc));// charlie

      // Previous snapshot: Alice=10, Bob=5, Charlie=10
      when(rankingRepository.findDistinctDates()).thenReturn(List.of(t1));
      when(rankingRepository.findAllByDate(t1)).thenReturn(new ArrayList<>(List.of(
          ranking(alice, 10, t1),
          ranking(bob, 5, t1),
          ranking(charlie, 10, t1)
      )));

      List<RankingDataNew> result = rankingService.getLeaderboard();

      RankingDataNew aliceData = findByAccountId(result, 1L);
      RankingDataNew bobData = findByAccountId(result, 2L);
      RankingDataNew charlieData = findByAccountId(result, 3L);

      // Current (live): Bob(20)→1, Alice(15)→2, Charlie(10)→3
      assertThat(bobData.getRank()).isEqualTo(1);
      assertThat(aliceData.getRank()).isEqualTo(2);
      assertThat(charlieData.getRank()).isEqualTo(3);

      // Previous (snapshot): Alice(10)→1, Charlie(10)→1, Bob(5)→2
      assertThat(aliceData.getPrevRank()).isEqualTo(1);
      assertThat(charlieData.getPrevRank()).isEqualTo(1);
      assertThat(bobData.getPrevRank()).isEqualTo(2);

      assertThat(aliceData.getPrevPoints()).isEqualTo(10);
      assertThat(bobData.getPrevPoints()).isEqualTo(5);
    }

    @Test
    @DisplayName("rank movement: up, down, and unchanged")
    void rankMovement() {
      Timestamp t1 = Timestamp.valueOf(LocalDateTime.of(2026, 6, 15, 12, 0));

      // Live: Bob=15, Alice=10, Charlie=5
      when(accountRepository.findAllActiveUsers()).thenReturn(List.of(alice, bob, charlie));
      when(betService.findByUserBetIdAccountId(anyLong())).thenReturn(List.of());
      var aliceCalc = CalculatedUserBet.builder().matchResultPoints(10).exactScorePoints(0).correctQualifierPoints(0).build();
      var bobCalc = CalculatedUserBet.builder().matchResultPoints(15).exactScorePoints(0).correctQualifierPoints(0).build();
      var charlieCalc = CalculatedUserBet.builder().matchResultPoints(5).exactScorePoints(0).correctQualifierPoints(0).build();
      when(resultsService.calculateBetsForUser(anyList()))
          .thenReturn(List.of(aliceCalc))
          .thenReturn(List.of(bobCalc))
          .thenReturn(List.of(charlieCalc));

      // Previous snapshot: Alice=10, Bob=8, Charlie=5
      when(rankingRepository.findDistinctDates()).thenReturn(List.of(t1));
      when(rankingRepository.findAllByDate(t1)).thenReturn(new ArrayList<>(List.of(
          ranking(alice, 10, t1),
          ranking(bob, 8, t1),
          ranking(charlie, 5, t1)
      )));

      List<RankingDataNew> result = rankingService.getLeaderboard();

      RankingDataNew aliceData = findByAccountId(result, 1L);
      RankingDataNew bobData = findByAccountId(result, 2L);
      RankingDataNew charlieData = findByAccountId(result, 3L);

      // Bob moved up: prevRank=2 → rank=1
      assertThat(bobData.getRank()).isEqualTo(1);
      assertThat(bobData.getPrevRank()).isEqualTo(2);

      // Alice moved down: prevRank=1 → rank=2
      assertThat(aliceData.getRank()).isEqualTo(2);
      assertThat(aliceData.getPrevRank()).isEqualTo(1);

      // Charlie unchanged: prevRank=3 → rank=3
      assertThat(charlieData.getRank()).isEqualTo(3);
      assertThat(charlieData.getPrevRank()).isEqualTo(3);
    }
  }

  // ── Dense ranking tests ─────────────────────────────────────────────────

  @Nested
  @DisplayName("Dense ranking (tied users)")
  class DenseRanking {

    @Test
    @DisplayName("all tied → everyone gets rank 1")
    void allTied() {
      when(accountRepository.findAllActiveUsers()).thenReturn(List.of(alice, bob, charlie));
      when(betService.findByUserBetIdAccountId(anyLong())).thenReturn(List.of());
      var calc = CalculatedUserBet.builder().matchResultPoints(10).exactScorePoints(0).correctQualifierPoints(0).build();
      when(resultsService.calculateBetsForUser(anyList())).thenReturn(List.of(calc));
      when(rankingRepository.findDistinctDates()).thenReturn(List.of());

      List<RankingDataNew> result = rankingService.getLeaderboard();

      assertThat(result).allMatch(r -> r.getRank() == 1);
      assertThat(result).allMatch(r -> r.getPoints() == 10);
    }

    @Test
    @DisplayName("dense ranking: two tied at top, next gets rank 2 (not 3)")
    void twoTiedAtTop() {
      when(accountRepository.findAllActiveUsers()).thenReturn(List.of(alice, bob, charlie));
      when(betService.findByUserBetIdAccountId(anyLong())).thenReturn(List.of());
      var calc10 = CalculatedUserBet.builder().matchResultPoints(10).exactScorePoints(0).correctQualifierPoints(0).build();
      var calc5 = CalculatedUserBet.builder().matchResultPoints(5).exactScorePoints(0).correctQualifierPoints(0).build();
      when(resultsService.calculateBetsForUser(anyList()))
          .thenReturn(List.of(calc10))   // alice
          .thenReturn(List.of(calc10))   // bob
          .thenReturn(List.of(calc5));   // charlie
      when(rankingRepository.findDistinctDates()).thenReturn(List.of());

      List<RankingDataNew> result = rankingService.getLeaderboard();

      RankingDataNew aliceData = findByAccountId(result, 1L);
      RankingDataNew bobData = findByAccountId(result, 2L);
      RankingDataNew charlieData = findByAccountId(result, 3L);

      assertThat(aliceData.getRank()).isEqualTo(1);
      assertThat(bobData.getRank()).isEqualTo(1);
      // Dense ranking: next rank after tie is 2, not 3
      assertThat(charlieData.getRank()).isEqualTo(2);
    }
  }

  // ── Live ranking (no snapshots) ─────────────────────────────────────────

  @Nested
  @DisplayName("Live ranking (no snapshots)")
  class LiveRanking {

    @Test
    @DisplayName("live ranking computes points from bets correctly")
    void liveRankingFromBets() {
      when(accountRepository.findAllActiveUsers()).thenReturn(List.of(alice, bob, charlie));
      when(betService.findByUserBetIdAccountId(anyLong())).thenReturn(List.of());
      var aliceCalc = CalculatedUserBet.builder().matchResultPoints(7).exactScorePoints(5).correctQualifierPoints(0).build();
      var bobCalc = CalculatedUserBet.builder().matchResultPoints(3).exactScorePoints(0).correctQualifierPoints(4).build();
      var charlieCalc = CalculatedUserBet.builder().matchResultPoints(0).exactScorePoints(0).correctQualifierPoints(0).build();
      when(resultsService.calculateBetsForUser(anyList()))
          .thenReturn(List.of(aliceCalc))
          .thenReturn(List.of(bobCalc))
          .thenReturn(List.of(charlieCalc));
      when(rankingRepository.findDistinctDates()).thenReturn(List.of());

      List<RankingDataNew> result = rankingService.getLeaderboard();

      assertThat(result).hasSize(3);
      RankingDataNew aliceData = findByAccountId(result, 1L);
      RankingDataNew bobData = findByAccountId(result, 2L);
      RankingDataNew charlieData = findByAccountId(result, 3L);

      assertThat(aliceData.getPoints()).isEqualTo(12);  // 7 + 5
      assertThat(aliceData.getRank()).isEqualTo(1);

      assertThat(bobData.getPoints()).isEqualTo(7);     // 3 + 4
      assertThat(bobData.getRank()).isEqualTo(2);

      assertThat(charlieData.getPoints()).isEqualTo(0);
      assertThat(charlieData.getRank()).isEqualTo(3);
    }
  }

  // ── Ordering ────────────────────────────────────────────────────────────

  @Nested
  @DisplayName("Result ordering")
  class Ordering {

    @Test
    @DisplayName("results are sorted by points descending")
    void sortedByPointsDescending() {
      when(accountRepository.findAllActiveUsers()).thenReturn(List.of(alice, bob, charlie));
      when(betService.findByUserBetIdAccountId(anyLong())).thenReturn(List.of());
      var calc20 = CalculatedUserBet.builder().matchResultPoints(20).exactScorePoints(0).correctQualifierPoints(0).build();
      var calc10 = CalculatedUserBet.builder().matchResultPoints(10).exactScorePoints(0).correctQualifierPoints(0).build();
      var calc5 = CalculatedUserBet.builder().matchResultPoints(5).exactScorePoints(0).correctQualifierPoints(0).build();
      when(resultsService.calculateBetsForUser(anyList()))
          .thenReturn(List.of(calc20))   // alice
          .thenReturn(List.of(calc10))   // bob
          .thenReturn(List.of(calc5));   // charlie
      when(rankingRepository.findDistinctDates()).thenReturn(List.of());

      List<RankingDataNew> result = rankingService.getLeaderboard();

      assertThat(result.get(0).getAccount().getId()).isEqualTo(1L); // Alice: 20
      assertThat(result.get(1).getAccount().getId()).isEqualTo(2L); // Bob: 10
      assertThat(result.get(2).getAccount().getId()).isEqualTo(3L); // Charlie: 5
    }
  }

  // ── Utility ─────────────────────────────────────────────────────────────

  private RankingDataNew findByAccountId(List<RankingDataNew> data, Long accountId) {
    return data.stream()
        .filter(r -> r.getAccount().getId().equals(accountId))
        .findFirst()
        .orElseThrow(() -> new AssertionError("No ranking data for accountId=" + accountId));
  }
}
