package com.ab.worldcup.bet;

import com.ab.worldcup.match.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {
    Bet findByMatchId(Long matchId);
    List<Bet> findAllByStageIdAndType(Stage stage, BetType type);
    List<Bet> findAllByType(BetType type);
}