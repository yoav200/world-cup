package com.ab.worldcup.bet;

import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserBetRepository extends JpaRepository<UserBet, UserBetId> {

    List<UserBet> findByUserBetIdBet(Bet bet);

    List<UserBet> findByUserBetIdBetAndQualifier(Bet bet, Team qualifier);

    List<UserBet> findByUserBetIdAccountId(Long accountId);

    @Query(value = "SELECT u.* FROM user_bet u, bet b, group_match g WHERE u.betid = b.id and  b.matchId = g.matchId and g.groupId=?1", nativeQuery = true)
    List<UserBet> findUserBetByGroup(String groupId);
}
