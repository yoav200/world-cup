package com.ab.worldcup.results;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetType;
import com.ab.worldcup.bet.UserBet;
import com.ab.worldcup.knockout.KnockoutTeam;
import com.ab.worldcup.knockout.KnockoutTeamRepository;
import com.ab.worldcup.match.Stage;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.web.model.RankingData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ResultsService {

    @Autowired
    private MatchResultRepository matchResultRepository;

    @Autowired
    private QualifierRepository qualifierRepository;

    @Autowired
    private KnockoutTeamRepository knockoutTeamRepository;

    public RankingData getRankingForAccount(Account account, List<UserBet> betForAccount, int betsCount) {
        List<CalculatedUserBet> calculatedUserBets = calculateBetsForUser(betForAccount);
        Integer userBetProgressPercentage = calculatedUserBets.size() * 100 / betsCount;
        Integer totalPointsForAccount = calculatedUserBets.stream().mapToInt(CalculatedUserBet::getTotalPoints).sum();
        return RankingData.builder()
                .account(account)
                .totalPoints(totalPointsForAccount)
                .userBets(calculatedUserBets)
                .betCompletionPercentage(userBetProgressPercentage)
                .build();
    }


    public List<CalculatedUserBet> calculateBetsForUser(List<UserBet> betForAccount) {
        List<CalculatedUserBet> calculatedUserBets = new ArrayList<>();
        Map<Long, MatchResult> resultMap = getAllMatchResults().stream().collect(Collectors.toMap(MatchResult::getMatchId, Function.identity()));

        for (UserBet userBet : betForAccount) {
            int correctWinnerPoints = 0;
            int exactScorePoints = 0;
            int correctQualifierPoints = 0;

            Bet bet = userBet.getUserBetId().getBet();
            if (BetType.MATCH.equals(bet.getType())) {
                MatchResult matchResult = resultMap.get(bet.getMatchId());
                // if match finished, calculate the points
                if (matchResult != null) {
                    correctWinnerPoints = getPointsForMatchResultCorrectness(userBet, matchResult);
                    exactScorePoints = getPointsForExactScoreCorrectness(userBet, matchResult);
                }
            } else {
                correctQualifierPoints = getPointsForQualifierCorrectness(userBet, bet.getStageId());
            }

            calculatedUserBets.add(CalculatedUserBet.builder()
                    .betType(bet.getType())
                    .matchResultPoints(correctWinnerPoints)
                    .exactScorePoints(exactScorePoints)
                    .correctQualifierPoints(correctQualifierPoints)
                    .userBet(userBet)
                    .build());
        }
        return calculatedUserBets;
    }

    /**
     * @return number of points gained in can user bet is correct,otherwise 0
     */
    private int getPointsForMatchResultCorrectness(UserBet userBet, MatchResult matchResult) {
        if (isSameWinner(userBet, matchResult)) {
            return PointsConfig.getCorrectWinnerPoints();
        }
        return 0;
    }

    private int getPointsForExactScoreCorrectness(UserBet userBet, MatchResult matchResult) {
        if (isExactScore(userBet, matchResult)) {
            return PointsConfig.getExactScorePoints();
        }
        return 0;
    }

    private int getPointsForQualifierCorrectness(UserBet userBet, Stage stage) {
        // check if qualifier according to user bet exists
        Qualifier qualifier = qualifierRepository.findByTeamAndStageId(userBet.getQualifier(), stage);
        if (qualifier != null) {
            return PointsConfig.getQualifierPoints(stage);
        }
        return 0;
    }

    private boolean isExactScore(ResultInterface userBet, ResultInterface matchResult) {
        return userBet.scoreEquals(matchResult);
    }

    private boolean isSameWinner(ResultInterface userBet, ResultInterface matchResult) {
        return userBet.winnerEquals(matchResult);
    }

    @CacheEvict(value = "allMatchResultsCache", allEntries = true)
    public MatchResult save(MatchResult result) {
        return matchResultRepository.save(result);
    }

    public void saveKnockoutTeam(KnockoutTeam knockoutTeam) {
        knockoutTeamRepository.save(knockoutTeam);
    }

    @CacheEvict(value = "allQualifiersCache", allEntries = true)
    public void saveQualifier(Qualifier qualifier) {
        qualifierRepository.save(qualifier);
    }

    public List<MatchResult> getMatchResultForGroup(Group group) {
        return matchResultRepository.findMatchResultByGroup(group.toString());
    }

    @Cacheable("allQualifiersCache")
    public List<Qualifier> getAllQualifiers() {
        return qualifierRepository.findAll();
    }

    @Cacheable("allMatchResultsCache")
    public List<MatchResult> getAllMatchResults() {
        return matchResultRepository.findAll();
    }

    public MatchResult findMatchResultByMatchId(Long matchId) {
        return matchResultRepository.findOne(matchId);
    }
}
