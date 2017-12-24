package com.ab.worldcup.bet;

import com.ab.worldcup.match.Match;
import com.ab.worldcup.match.Stage;
import com.ab.worldcup.team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BetService {
    @Autowired
    BetRepository betRepository;

    @Autowired
    UserBetRepository userBetRepository;

    public Bet getBetByMatch(Match match){
        Long matchId = match.getMatchId();
        return betRepository.findByMatchId(matchId);
    }

    public List<UserBet> getUserBetsByBet(Bet bet){
        UserBetId userBetId = new UserBetId();
        userBetId.setBet(bet);
        return userBetRepository.findByUserBetIdBet(bet);
    }


    /**
     * This method will return all UserBet in which <code>team</code> qualified to <code>stage</code>
     * @return List<UserBet>
     */
    public List<UserBet> getAllCorrectUserBetForQualifiedTeam(Team team, Stage stage){
        List<Bet> allQualificationBetsForThisStage = betRepository.findAllByStageIdAndType(stage, BetType.QUALIFIER);
        List<UserBet> correctUserBets = new ArrayList<>();
        for (Bet bet : allQualificationBetsForThisStage) {
            correctUserBets.addAll(userBetRepository.findByUserBetIdBetAndQualifier(bet, team));
        }
        return correctUserBets;
    }

}
