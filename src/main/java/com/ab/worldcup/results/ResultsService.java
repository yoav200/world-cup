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
import java.util.Optional;
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

    public RankingData getRankingForAccount(Account account, List<UserBet> betForAccount, int qualifierBetsCount, int groupMatchesCount, int knockoutMatchesCount) {
        List<CalculatedUserBet> calculatedUserBets = calculateBetsForUser(betForAccount);
        int qualifierBetProgressPercentage = (int)calculatedUserBets.stream().filter(t -> t.getBetType().equals(BetType.QUALIFIER)).count() * 100 / qualifierBetsCount;
        int groupMatchesBetProgressPercentage = (int)calculatedUserBets.stream().filter(t -> t.getBetType().equals(BetType.MATCH) && t.getUserBet().getUserBetId().getBet().getStageId().equals(Stage.GROUP)).count() * 100 / groupMatchesCount;
        int knockoutMatchesBetProgressPercentage = (int)calculatedUserBets.stream().filter(t -> t.getBetType().equals(BetType.MATCH) && !t.getUserBet().getUserBetId().getBet().getStageId().equals(Stage.GROUP)).count() * 100 / knockoutMatchesCount;

        Integer totalPointsForAccount = calculatedUserBets.stream().mapToInt(CalculatedUserBet::getTotalPoints).sum();
        return RankingData.builder()
                .account(account)
                .totalPoints(totalPointsForAccount)
                //.userBets(calculatedUserBets)
                .qualifierBetCompletionPercentage(qualifierBetProgressPercentage)
                .groupMatchesBetCompletionPercentage(groupMatchesBetProgressPercentage)
                .knockoutMatchesBetCompletionPercentage(knockoutMatchesBetProgressPercentage)
                .build();
    }


    public List<CalculatedUserBet> calculateBetsForUser(List<UserBet> betForAccount) {
        List<CalculatedUserBet> calculatedUserBets = new ArrayList<>();
        Map<Long, MatchResult> resultMap = getAllMatchResults().stream().collect(Collectors.toMap(MatchResult::getMatchId, Function.identity()));

        for (UserBet userBet : betForAccount) {
            int correctWinnerPoints = 0;
            int exactScorePoints = 0;
            int correctQualifierPoints = 0;
            BetCorrectnessTypeEnum isQualifierDetermined = BetCorrectnessTypeEnum.Not_Yet_Determined;
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
                isQualifierDetermined = isQualifierDetermined((correctQualifierPoints != 0), bet.getStageId());
            }

            calculatedUserBets.add(CalculatedUserBet.builder()
                    .betType(bet.getType())
                    .matchResultPoints(correctWinnerPoints)
                    .exactScorePoints(exactScorePoints)
                    .correctQualifierPoints(correctQualifierPoints)
                    .isCorrectQualifier(isQualifierDetermined)
                    .userBet(userBet)
                    .build());
        }
        return calculatedUserBets;
    }

    private BetCorrectnessTypeEnum isQualifierDetermined(boolean correctQualifier, Stage stage) {
        if (correctQualifier) {
            return BetCorrectnessTypeEnum.True;
        } else {
            List<Qualifier> qualifierForStage = qualifierRepository.findByStageId(stage);
            if (qualifierForStage != null && qualifierForStage.size() == stage.getNumberOfQualifiersForStage()) {
                return BetCorrectnessTypeEnum.False;
            }
            return BetCorrectnessTypeEnum.Not_Yet_Determined;
        }
    }

    /**
     * @return number of points gained in can user bet is correct,otherwise 0
     */
    private int getPointsForMatchResultCorrectness(UserBet userBet, MatchResult matchResult) {
        return isSameWinner(userBet, matchResult) ? PointsConfig.getCorrectWinnerPoints() : 0;
    }

    private int getPointsForExactScoreCorrectness(UserBet userBet, MatchResult matchResult) {
        return isExactScore(userBet, matchResult) ? PointsConfig.getExactScorePoints() : 0;
    }

    /**
     * check if qualifier according to user bet exists
     *
     * @param userBet user bet
     * @param stage   the stage
     * @return point gain for correct bet according to stage or 0 if bet is wrong
     */
    private int getPointsForQualifierCorrectness(UserBet userBet, Stage stage) {
        Qualifier qualifier = qualifierRepository.findByTeamAndStageId(userBet.getQualifier(), stage);
        return Optional.ofNullable(qualifier).map(q -> PointsConfig.getQualifierPoints(stage)).orElse(0);
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
