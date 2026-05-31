package com.ab.worldcup.web.api;

import com.ab.worldcup.coins.CoinEvent;
import com.ab.worldcup.coins.CoinEventType;
import com.ab.worldcup.coins.CoinService;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/coins")
public class CoinController {

  private final CoinService coinService;

  @GetMapping("/bucket")
  public Map<String, Object> getBucket() {
    return Map.of(
        "total", coinService.getBucketTotal(),
        "breakdown", coinService.getBucketBreakdown(),
        "distribution", coinService.getDistribution()
    );
  }

  @GetMapping("/events")
  public List<CoinEvent> getEvents() {
    return coinService.getRecentEvents();
  }
}
