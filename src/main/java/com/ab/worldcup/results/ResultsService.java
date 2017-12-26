package com.ab.worldcup.results;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountRepository;
import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.bet.BetType;
import com.ab.worldcup.bet.UserBet;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultsService {
    @Autowired
    MatchResultRepository matchResultRepository;

    @Autowired
    QualifierRepository qualifierRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    BetService betService;

    public List<Pair<Account,Integer>> getLeaderboard(){
        List<Pair<Account,Integer>> leaderboardList = new ArrayList<>();
        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            List<CalculatedUserBet> calculatedUserBets = calculateBetsForUser(account);
            Integer totalPointsForAccount = calculatedUserBets.stream().mapToInt(i -> i.getPointsForBet()).sum();
            leaderboardList.add(Pair.of(account,totalPointsForAccount));
        }
        leaderboardList.sort((o1, o2) -> o1.getRight() > o2.getRight() ? 1 : -1);
        return leaderboardList;
    }

    @CachePut(cacheNames="CalculatedUserBets", key = "account.id")
    public List<CalculatedUserBet> calculateBetsForUser(Account account) {
        List<CalculatedUserBet> calculatedUserBets = new ArrayList<>();
        List<UserBet> betForAccount = betService.findByUserBetIdAccountId(account.getId());

        for (UserBet userBet : betForAccount) {
            boolean correctWinner = false;
            boolean exactScore = false;
            boolean correctQualifier = false;

            Bet bet = userBet.getUserBetId().getBet();
            if(BetType.MATCH.equals(bet.getType())){
                MatchResult matchResult = matchResultRepository.findOne(bet.getMatchId());
                correctWinner = isBetWinnerCorrect(userBet,matchResult);
                exactScore = isBetExactScoreCorrect(userBet,matchResult);
            } // else, qualifier bet
            else{
                // check if qualifier according to user bet exists
                Qualifier qualifier = qualifierRepository.findByTeamAndStageId(userBet.getQualifier(), bet.getStageId());
                if(qualifier != null){
                    correctQualifier = true;
                }
            }
            CalculatedUserBet calculatedUserBet = CalculatedUserBet.builder().
                    betType(bet.getType()).
                    isMatchResultCorrect(correctWinner).
                    isExactScore(exactScore).
                    isCorrectQualifier(correctQualifier).
                    userBet(userBet).
                    build();

            // TODO: calculate points

            calculatedUserBets.add(calculatedUserBet);
        }
        return calculatedUserBets;
    }

    private boolean isBetExactScoreCorrect(ResultInterface userBet, ResultInterface matchResult) {
        return userBet.scoreEquals(matchResult);
    }

    private boolean isBetWinnerCorrect(ResultInterface userBet, ResultInterface matchResult) {
        return userBet.winnerEquals(matchResult);
    }
}
