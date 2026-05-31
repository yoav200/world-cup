package com.ab.worldcup.coins;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoinServiceTest {

  @Mock private CoinEventRepository coinEventRepository;
  @Mock private CoinConfig coinConfig;

  @InjectMocks private CoinService coinService;

  private CoinConfig.Rates rates;

  @BeforeEach
  void setUp() {
    rates = new CoinConfig.Rates();
  }

  @Nested
  @DisplayName("emitLoginEvent")
  class LoginEvent {

    @Test
    @DisplayName("emits LOGIN event when no login today")
    void emitsLoginEvent() {
      when(coinEventRepository.existsByEventTypeAndAccountIdAndCreatedAtBetween(
          eq(CoinEventType.LOGIN), eq(1L), any(), any())).thenReturn(false);
      when(coinConfig.getRateForEvent(CoinEventType.LOGIN)).thenReturn(10);

      coinService.emitLoginEvent(1L);

      ArgumentCaptor<CoinEvent> captor = ArgumentCaptor.forClass(CoinEvent.class);
      verify(coinEventRepository).save(captor.capture());
      CoinEvent saved = captor.getValue();
      assertThat(saved.getEventType()).isEqualTo(CoinEventType.LOGIN);
      assertThat(saved.getCoins()).isEqualTo(10);
      assertThat(saved.getAccountId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("skips if already logged in today")
    void skipsIfAlreadyLoggedInToday() {
      when(coinEventRepository.existsByEventTypeAndAccountIdAndCreatedAtBetween(
          eq(CoinEventType.LOGIN), eq(1L), any(), any())).thenReturn(true);

      coinService.emitLoginEvent(1L);

      verify(coinEventRepository, never()).save(any());
    }
  }

  @Nested
  @DisplayName("emitBetPlacedEvent")
  class BetPlacedEvent {

    @Test
    @DisplayName("emits BET_PLACED event for new bet")
    void emitsBetPlacedEvent() {
      when(coinEventRepository.existsByEventTypeAndAccountIdAndReferenceId(
          CoinEventType.BET_PLACED, 1L, 42L)).thenReturn(false);
      when(coinConfig.getRateForEvent(CoinEventType.BET_PLACED)).thenReturn(25);

      coinService.emitBetPlacedEvent(1L, 42L);

      ArgumentCaptor<CoinEvent> captor = ArgumentCaptor.forClass(CoinEvent.class);
      verify(coinEventRepository).save(captor.capture());
      CoinEvent saved = captor.getValue();
      assertThat(saved.getEventType()).isEqualTo(CoinEventType.BET_PLACED);
      assertThat(saved.getCoins()).isEqualTo(25);
      assertThat(saved.getAccountId()).isEqualTo(1L);
      assertThat(saved.getReferenceId()).isEqualTo(42L);
    }

    @Test
    @DisplayName("skips duplicate bet event")
    void skipsDuplicate() {
      when(coinEventRepository.existsByEventTypeAndAccountIdAndReferenceId(
          CoinEventType.BET_PLACED, 1L, 42L)).thenReturn(true);

      coinService.emitBetPlacedEvent(1L, 42L);

      verify(coinEventRepository, never()).save(any());
    }
  }

  @Nested
  @DisplayName("emitGlobalEvent")
  class GlobalEvent {

    @Test
    @DisplayName("emits START_SEED once")
    void emitsStartSeed() {
      when(coinEventRepository.existsByEventType(CoinEventType.START_SEED)).thenReturn(false);
      when(coinConfig.getRateForEvent(CoinEventType.START_SEED)).thenReturn(5000);

      coinService.emitGlobalEvent(CoinEventType.START_SEED);

      ArgumentCaptor<CoinEvent> captor = ArgumentCaptor.forClass(CoinEvent.class);
      verify(coinEventRepository).save(captor.capture());
      CoinEvent saved = captor.getValue();
      assertThat(saved.getEventType()).isEqualTo(CoinEventType.START_SEED);
      assertThat(saved.getCoins()).isEqualTo(5000);
      assertThat(saved.getAccountId()).isNull();
    }

    @Test
    @DisplayName("skips if global event already emitted")
    void skipsIfAlreadyEmitted() {
      when(coinEventRepository.existsByEventType(CoinEventType.START_SEED)).thenReturn(true);

      coinService.emitGlobalEvent(CoinEventType.START_SEED);

      verify(coinEventRepository, never()).save(any());
    }

    @Test
    @DisplayName("rejects non-global event types")
    void rejectsNonGlobalType() {
      assertThatThrownBy(() -> coinService.emitGlobalEvent(CoinEventType.LOGIN))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("emits SECOND_ROUND_BONUS")
    void emitsSecondRoundBonus() {
      when(coinEventRepository.existsByEventType(CoinEventType.SECOND_ROUND_BONUS)).thenReturn(false);
      when(coinConfig.getRateForEvent(CoinEventType.SECOND_ROUND_BONUS)).thenReturn(3000);

      coinService.emitGlobalEvent(CoinEventType.SECOND_ROUND_BONUS);

      ArgumentCaptor<CoinEvent> captor = ArgumentCaptor.forClass(CoinEvent.class);
      verify(coinEventRepository).save(captor.capture());
      assertThat(captor.getValue().getCoins()).isEqualTo(3000);
    }

    @Test
    @DisplayName("emits END_BONUS")
    void emitsEndBonus() {
      when(coinEventRepository.existsByEventType(CoinEventType.END_BONUS)).thenReturn(false);
      when(coinConfig.getRateForEvent(CoinEventType.END_BONUS)).thenReturn(2000);

      coinService.emitGlobalEvent(CoinEventType.END_BONUS);

      ArgumentCaptor<CoinEvent> captor = ArgumentCaptor.forClass(CoinEvent.class);
      verify(coinEventRepository).save(captor.capture());
      assertThat(captor.getValue().getCoins()).isEqualTo(2000);
    }
  }

  @Nested
  @DisplayName("getBucketTotal")
  class BucketTotal {

    @Test
    @DisplayName("returns sum from repository")
    void returnsSumFromRepository() {
      when(coinEventRepository.sumAllCoins()).thenReturn(12345L);

      assertThat(coinService.getBucketTotal()).isEqualTo(12345L);
    }
  }

  @Nested
  @DisplayName("emitQualifierBetPlacedEvent")
  class QualifierBetEvent {

    @Test
    @DisplayName("emits QUALIFIER_BET_PLACED once per user")
    void emitsQualifierBetEvent() {
      when(coinEventRepository.existsByEventTypeAndAccountIdAndReferenceId(
          CoinEventType.QUALIFIER_BET_PLACED, 1L, null)).thenReturn(false);
      when(coinConfig.getRateForEvent(CoinEventType.QUALIFIER_BET_PLACED)).thenReturn(50);

      coinService.emitQualifierBetPlacedEvent(1L);

      ArgumentCaptor<CoinEvent> captor = ArgumentCaptor.forClass(CoinEvent.class);
      verify(coinEventRepository).save(captor.capture());
      assertThat(captor.getValue().getEventType()).isEqualTo(CoinEventType.QUALIFIER_BET_PLACED);
      assertThat(captor.getValue().getCoins()).isEqualTo(50);
    }

    @Test
    @DisplayName("skips if already emitted for user")
    void skipsIfAlreadyEmitted() {
      when(coinEventRepository.existsByEventTypeAndAccountIdAndReferenceId(
          CoinEventType.QUALIFIER_BET_PLACED, 1L, null)).thenReturn(true);

      coinService.emitQualifierBetPlacedEvent(1L);

      verify(coinEventRepository, never()).save(any());
    }
  }
}
