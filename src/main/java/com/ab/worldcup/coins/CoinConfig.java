package com.ab.worldcup.coins;

import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "coins")
public class CoinConfig {

  private Rates rates = new Rates();

  private Map<Integer, Integer> distribution = Map.of();

  @Data
  public static class Rates {
    private int login = 10;
    private int betPlaced = 25;
    private int qualifierBetPlaced = 50;
    private int fullRound = 100;
    private int startSeed = 5000;
    private int secondRoundBonus = 3000;
    private int endBonus = 2000;
  }

  public int getRateForEvent(CoinEventType type) {
    return switch (type) {
      case LOGIN -> rates.getLogin();
      case BET_PLACED -> rates.getBetPlaced();
      case QUALIFIER_BET_PLACED -> rates.getQualifierBetPlaced();
      case FULL_ROUND -> rates.getFullRound();
      case START_SEED -> rates.getStartSeed();
      case SECOND_ROUND_BONUS -> rates.getSecondRoundBonus();
      case END_BONUS -> rates.getEndBonus();
    };
  }
}
