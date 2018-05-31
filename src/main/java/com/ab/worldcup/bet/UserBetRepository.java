package com.ab.worldcup.bet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserBetRepository extends JpaRepository<UserBet, UserBetId> {

    UserBet findByUserBetIdAccountIdAndUserBetIdBetId(Long accountId, Long betId);

    List<UserBet> findByUserBetIdAccountId(Long accountId);

    List<UserBet> findByUserBetIdBetId(Long betId);

    @Query(value = "SELECT u.* FROM user_bet u, bet b, group_match g WHERE u.betid = b.id and  b.matchId = g.matchId and g.groupId=?1 and u.accountId=?2", nativeQuery = true)
    List<UserBet> findUserBetByGroup(String groupId, Long accountId);
}
