package com.ab.worldcup.bet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountStatus;
import com.ab.worldcup.config.ApplicationConfig;
import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.match.Match;
import com.ab.worldcup.match.MatchService;
import com.ab.worldcup.match.Stage;
import com.ab.worldcup.results.ResultsService;
import com.ab.worldcup.web.model.UserBetData;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BetServiceTest {

  @Mock private BetRepository betRepository;
  @Mock private UserBetRepository userBetRepository;
  @Mock private GroupService groupService;
  @Mock private MatchService matchService;
  @Mock private ResultsService resultsService;
  @Mock private ApplicationConfig applicationConfig;
  @Mock private ApplicationEventPublisher eventPublisher;

  @InjectMocks private BetService betService;

  private Account alice;

  @BeforeEach
  void setUp() {
    alice = Account.builder().id(1L).email("alice@test.com")
        .firstName("Alice").lastName("A").enabled(true).status(AccountStatus.ACTIVE).build();
  }

  // ── Helpers ─────────────────────────────────────────────────────────────

  private Bet createBet(Long id, Long matchId, BetType type, LocalDateTime lockTime) {
    try {
      Bet bet = new Bet();
      setField(bet, "id", id);
      setField(bet, "matchId", matchId);
      setField(bet, "type", type);
      setField(bet, "stageId", Stage.GROUP);
      setField(bet, "lockTime", Timestamp.valueOf(lockTime));
      setField(bet, "description", "Test bet " + id);
      return bet;
    } catch (Exception e) {
      throw new RuntimeException("Failed to create test Bet", e);
    }
  }

  private void setField(Object obj, String fieldName, Object value) throws Exception {
    Field field = obj.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(obj, value);
  }

  private UserBetData createUserBetData(Long betId, int homeGoals, int awayGoals) {
    UserBetData data = new UserBetData();
    data.setBetId(betId);
    data.setHomeTeamGoals(homeGoals);
    data.setAwayTeamGoals(awayGoals);
    return data;
  }

  // ── Bet.isLock() ────────────────────────────────────────────────────────

  @Nested
  @DisplayName("Bet lock time")
  class BetLockTime {

    @Test
    @DisplayName("bet with lock time in the future → not locked")
    void futureLockTime_notLocked() {
      Bet bet = createBet(1L, 1L, BetType.MATCH, LocalDateTime.now().plusHours(2));
      assertThat(bet.isLock()).isFalse();
    }

    @Test
    @DisplayName("bet with lock time in the past → locked")
    void pastLockTime_locked() {
      Bet bet = createBet(1L, 1L, BetType.MATCH, LocalDateTime.now().minusHours(2));
      assertThat(bet.isLock()).isTrue();
    }

    @Test
    @DisplayName("bet with lock time just passed → locked")
    void justPassed_locked() {
      Bet bet = createBet(1L, 1L, BetType.MATCH, LocalDateTime.now().minusSeconds(1));
      assertThat(bet.isLock()).isTrue();
    }
  }

  // ── updateMatchBet: lock enforcement ────────────────────────────────────

  @Nested
  @DisplayName("updateMatchBet - lock enforcement")
  class UpdateMatchBetLock {

    @Test
    @DisplayName("placing bet after lock time → throws IllegalStateException")
    void betAfterLockTime_throws() {
      Bet lockedBet = createBet(1L, 1L, BetType.MATCH, LocalDateTime.now().minusHours(1));
      when(betRepository.findById(1L)).thenReturn(Optional.of(lockedBet));

      UserBetData data = createUserBetData(1L, 2, 1);

      assertThatThrownBy(() -> betService.updateMatchBet(alice, data))
          .isInstanceOf(IllegalStateException.class)
          .hasMessageContaining("Cannot place bet");

      verify(userBetRepository, never()).save(any());
    }

    @Test
    @DisplayName("placing bet before lock time → succeeds")
    void betBeforeLockTime_succeeds() {
      Bet openBet = createBet(1L, 1L, BetType.MATCH, LocalDateTime.now().plusHours(2));
      when(betRepository.findById(1L)).thenReturn(Optional.of(openBet));

      Match match = org.mockito.Mockito.mock(Match.class);
      when(match.getStageId()).thenReturn(Stage.GROUP);
      when(match.getHomeTeam()).thenReturn(org.mockito.Mockito.mock(com.ab.worldcup.team.Team.class));
      when(match.getAwayTeam()).thenReturn(org.mockito.Mockito.mock(com.ab.worldcup.team.Team.class));
      when(matchService.getMatchById(1L)).thenReturn(match);

      when(userBetRepository.findByUserBetIdAccountIdAndUserBetIdBetId(1L, 1L)).thenReturn(null);

      UserBet savedBet = new UserBet(new UserBetId(alice, openBet));
      savedBet.setHomeTeamGoals(2);
      savedBet.setAwayTeamGoals(1);
      when(userBetRepository.save(any(UserBet.class))).thenReturn(savedBet);

      UserBetData data = createUserBetData(1L, 2, 1);
      UserBet result = betService.updateMatchBet(alice, data);

      assertThat(result.getHomeTeamGoals()).isEqualTo(2);
      assertThat(result.getAwayTeamGoals()).isEqualTo(1);
      verify(userBetRepository).save(any(UserBet.class));
    }

    @Test
    @DisplayName("updating existing bet before lock time → updates in place")
    void updateExistingBet_succeeds() {
      Bet openBet = createBet(1L, 1L, BetType.MATCH, LocalDateTime.now().plusHours(2));
      when(betRepository.findById(1L)).thenReturn(Optional.of(openBet));

      Match match = org.mockito.Mockito.mock(Match.class);
      when(match.getStageId()).thenReturn(Stage.GROUP);
      when(match.getHomeTeam()).thenReturn(org.mockito.Mockito.mock(com.ab.worldcup.team.Team.class));
      when(match.getAwayTeam()).thenReturn(org.mockito.Mockito.mock(com.ab.worldcup.team.Team.class));
      when(matchService.getMatchById(1L)).thenReturn(match);

      UserBet existingBet = new UserBet(new UserBetId(alice, openBet));
      existingBet.setHomeTeamGoals(0);
      existingBet.setAwayTeamGoals(0);
      when(userBetRepository.findByUserBetIdAccountIdAndUserBetIdBetId(1L, 1L)).thenReturn(existingBet);
      when(userBetRepository.save(any(UserBet.class))).thenAnswer(inv -> inv.getArgument(0));

      UserBetData data = createUserBetData(1L, 3, 2);
      UserBet result = betService.updateMatchBet(alice, data);

      assertThat(result.getHomeTeamGoals()).isEqualTo(3);
      assertThat(result.getAwayTeamGoals()).isEqualTo(2);
    }
  }

  // ── deleteUserBet ───────────────────────────────────────────────────────

  @Nested
  @DisplayName("deleteUserBet")
  class DeleteUserBet {

    @Test
    @DisplayName("delete existing bet → succeeds")
    void deleteExisting_succeeds() {
      Bet bet = createBet(1L, 1L, BetType.MATCH, LocalDateTime.now().plusHours(2));
      UserBet existing = new UserBet(new UserBetId(alice, bet));
      when(userBetRepository.findByUserBetIdAccountIdAndUserBetIdBetId(1L, 1L)).thenReturn(existing);

      betService.deleteUserBet(1L, 1L);

      verify(userBetRepository).delete(existing);
    }

    @Test
    @DisplayName("delete non-existent bet → throws IllegalArgumentException")
    void deleteNonExistent_throws() {
      when(userBetRepository.findByUserBetIdAccountIdAndUserBetIdBetId(1L, 99L)).thenReturn(null);

      assertThatThrownBy(() -> betService.deleteUserBet(1L, 99L))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("User bet not found");
    }
  }

  // ── getBetStats ─────────────────────────────────────────────────────────

  @Nested
  @DisplayName("getBetStats")
  class GetBetStats {

    @Test
    @DisplayName("no bets → all percentages zero")
    void noBets_allZero() {
      when(userBetRepository.findByUserBetIdBetId(1L)).thenReturn(List.of());

      BetStatisticsData stats = betService.getBetStats(1L);

      assertThat(stats.getBetsOnHomeTeamPercent()).isZero();
      assertThat(stats.getBetsOnAwayTeamPercent()).isZero();
      assertThat(stats.getBetsOnDrawPercent()).isZero();
    }

    @Test
    @DisplayName("mixed bets → correct percentages")
    void mixedBets_correctPercentages() {
      Bet bet = createBet(1L, 1L, BetType.MATCH, LocalDateTime.now().plusHours(2));

      // 4 bets: 2 home wins, 1 away win, 1 draw
      UserBet homeWin1 = new UserBet(new UserBetId(alice, bet));
      homeWin1.setHomeTeamGoals(2);
      homeWin1.setAwayTeamGoals(0);

      Account bob = Account.builder().id(2L).email("bob@test.com")
          .firstName("Bob").lastName("B").enabled(true).build();
      UserBet homeWin2 = new UserBet(new UserBetId(bob, bet));
      homeWin2.setHomeTeamGoals(1);
      homeWin2.setAwayTeamGoals(0);

      Account charlie = Account.builder().id(3L).email("charlie@test.com")
          .firstName("Charlie").lastName("C").enabled(true).build();
      UserBet awayWin = new UserBet(new UserBetId(charlie, bet));
      awayWin.setHomeTeamGoals(0);
      awayWin.setAwayTeamGoals(2);

      Account dave = Account.builder().id(4L).email("dave@test.com")
          .firstName("Dave").lastName("D").enabled(true).build();
      UserBet draw = new UserBet(new UserBetId(dave, bet));
      draw.setHomeTeamGoals(1);
      draw.setAwayTeamGoals(1);

      when(userBetRepository.findByUserBetIdBetId(1L))
          .thenReturn(List.of(homeWin1, homeWin2, awayWin, draw));

      BetStatisticsData stats = betService.getBetStats(1L);

      assertThat(stats.getBetsOnHomeTeamPercent()).isEqualTo(50);  // 2/4 = 50%
      assertThat(stats.getBetsOnAwayTeamPercent()).isEqualTo(25);  // 1/4 = 25%
      assertThat(stats.getBetsOnDrawPercent()).isEqualTo(25);       // 1/4 = 25%
    }

    @Test
    @DisplayName("all draws → 100% draw")
    void allDraws() {
      Bet bet = createBet(1L, 1L, BetType.MATCH, LocalDateTime.now().plusHours(2));

      UserBet draw1 = new UserBet(new UserBetId(alice, bet));
      draw1.setHomeTeamGoals(1);
      draw1.setAwayTeamGoals(1);

      Account bob = Account.builder().id(2L).email("bob@test.com")
          .firstName("Bob").lastName("B").enabled(true).build();
      UserBet draw2 = new UserBet(new UserBetId(bob, bet));
      draw2.setHomeTeamGoals(0);
      draw2.setAwayTeamGoals(0);

      when(userBetRepository.findByUserBetIdBetId(1L)).thenReturn(List.of(draw1, draw2));

      BetStatisticsData stats = betService.getBetStats(1L);

      assertThat(stats.getBetsOnHomeTeamPercent()).isZero();
      assertThat(stats.getBetsOnAwayTeamPercent()).isZero();
      assertThat(stats.getBetsOnDrawPercent()).isEqualTo(100);
    }
  }
}
