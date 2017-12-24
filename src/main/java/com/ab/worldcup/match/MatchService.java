package com.ab.worldcup.match;

import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.bet.UserBet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MatchService {

    @Autowired
    GroupMatchRepository groupMatchRepository;

    @Autowired
    KnockoutMatchRepository knockoutMatchRepository;

    @Autowired
    GroupService groupService;

    @Autowired
    BetService betService;

    private final Logger logger = LoggerFactory.getLogger(MatchService.class);

    public void handleMatchFinish(Match match){
        Bet betForMatch = betService.getBetByMatch(match);
        List<UserBet> userBetsByBet = betService.getUserBetsByBet(betForMatch);
        for (UserBet userBet : userBetsByBet) {
            // TODO: calculate points and update leaderboard

        }

        Stage currentStage = betForMatch.getStageId();
        boolean groupFinished = true;

        if(Stage.GROUP.equals(currentStage)){
            GroupMatch groupMatch = groupMatchRepository.findOne(match.getMatchId());

            //groupFinished = groupService.isGroupFinished(groupMatch.getGroupId());
        }

        if(groupFinished) {
            Stage nextStage = currentStage.getNextStage();
            List<KnockoutMatch> knockoutMatchesInNextStage = knockoutMatchRepository.findAllByStageId(nextStage);
            for (KnockoutMatch knockoutMatch : knockoutMatchesInNextStage) {

                // TODO:
                // 1. update KnockTeam
                // 2. add Qualifier
                // 3. find all correct qualifier user bets and update leaderboard
            }
        }
    }

}
