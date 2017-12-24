package com.ab.worldcup.bet;

import com.ab.worldcup.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBetRepository extends JpaRepository<UserBet, UserBetId> {

    List<UserBet> findByUserBetIdBet(Bet bet);

    List<UserBet> findByUserBetIdBetAndQualifier(Bet bet, Team qualifier);

    List<UserBet> findByUserBetIdAccountId(Long accountId);
}
