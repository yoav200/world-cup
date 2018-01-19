package com.ab.worldcup.bet;

import com.ab.worldcup.match.Match;
import com.ab.worldcup.match.Stage;
import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BetService {

    private BetRepository betRepository;

    private UserBetRepository userBetRepository;

    @Autowired
    public BetService(UserBetRepository userBetRepository, BetRepository betRepository) {
        this.betRepository = betRepository;
        this.userBetRepository = userBetRepository;
    }

    public Bet getBetByMatch(Match match) {
        Long matchId = match.getMatchId();
        return betRepository.findByMatchId(matchId);
    }

    public List<UserBet> getUserBetsByBet(Bet bet) {
        UserBetId userBetId = new UserBetId();
        userBetId.setBet(bet);
        return userBetRepository.findByUserBetIdBet(bet);
    }


    /**
     * This method will return all UserBet in which <code>team</code> qualified to <code>stage</code>
     *
     * @return List<UserBet>
     */
    public List<UserBet> getAllCorrectUserBetForQualifiedTeam(Team team, Stage stage) {
        List<Bet> allQualificationBetsForThisStage = betRepository.findAllByStageIdAndType(stage, BetType.QUALIFIER);
        List<UserBet> correctUserBets = new ArrayList<>();
        for (Bet bet : allQualificationBetsForThisStage) {
            correctUserBets.addAll(userBetRepository.findByUserBetIdBetAndQualifier(bet, team));
        }
        return correctUserBets;
    }

    public List<UserBet> findByUserBetIdAccountId(Long accountId) {
        return userBetRepository.findByUserBetIdAccountId(accountId);
    }

    @Cacheable("bets")
    public List<Bet> getAllBets() {
        return betRepository.findAll();
    }


    public UserBet setUserBet(UserBet userBet) {
        return null;
    }


    public List<Match> addBetsToMatchs(List<Match> matches) {
        List<UserBet> userBets = userBetRepository.findAll();
        Map<Long, UserBet> betsMap = userBets.stream()
                .filter(b->b.getUserBetId().getBet().getMatchId()!=null)
                .collect(Collectors.toMap(b -> b.getUserBetId().getBet().getMatchId(), Function.identity()));
        matches.forEach(match-> match.setResult(betsMap.get(match.getMatchId())));
        return matches;
    }
}
