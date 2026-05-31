package com.ab.worldcup.coins;

import com.ab.worldcup.config.ApplicationConfig;
import com.ab.worldcup.events.BetPlacedEvent;
import com.ab.worldcup.events.MatchResultEnteredEvent;
import com.ab.worldcup.events.QualifierBetPlacedEvent;
import com.ab.worldcup.events.UserLoggedInEvent;
import com.ab.worldcup.match.Stage;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class CoinEventListener {

  private final CoinService coinService;
  private final ApplicationConfig applicationConfig;

  @EventListener
  public void onUserLoggedIn(UserLoggedInEvent event) {
    coinService.emitLoginEvent(event.accountId());

    if (!LocalDateTime.now().isBefore(applicationConfig.getStartDateTime())) {
      coinService.emitGlobalEvent(CoinEventType.START_SEED);
    }
  }

  @EventListener
  public void onBetPlaced(BetPlacedEvent event) {
    coinService.emitBetPlacedEvent(event.accountId(), event.betId());
  }

  @EventListener
  public void onQualifierBetPlaced(QualifierBetPlacedEvent event) {
    coinService.emitQualifierBetPlacedEvent(event.accountId());
  }

  @EventListener
  public void onMatchResultEntered(MatchResultEnteredEvent event) {
    if (!Stage.GROUP.equals(event.stage())) {
      coinService.emitGlobalEvent(CoinEventType.SECOND_ROUND_BONUS);
    }
    if (Stage.FINAL.equals(event.stage())) {
      coinService.emitGlobalEvent(CoinEventType.END_BONUS);
    }
  }
}
