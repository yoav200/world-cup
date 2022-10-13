package com.ab.worldcup.bet;

import com.ab.worldcup.match.Stage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, Long> {
    Bet findByMatchId(Long matchId);
    List<Bet> findAllByStageIdAndType(Stage stage, BetType type);
    List<Bet> findAllByType(BetType type);
}