package com.ab.worldcup.ranking;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountRepository;
import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.bet.BetType;
import com.ab.worldcup.bet.UserBet;
import com.ab.worldcup.match.Stage;
import com.ab.worldcup.results.ResultsService;
import com.ab.worldcup.web.model.RankingData;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RankingService {

    @Autowired
    private ResultsService resultsService;

    @Autowired
    private BetService betService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RankingRepository rankingRepository;

    public List<RankingDataNew> getLeaderboard() {
        Pair<Timestamp, Timestamp> datesToCompare = getRankingDatesToCompare();
        List<Ranking> current;
        List<Ranking> previous = null;
        if (datesToCompare.getLeft() != null) {
            current = rankingRepository.findAllByDate(datesToCompare.getLeft());
        } else {
            current = createRanking(LocalDateTime.now());
        }

        if (datesToCompare.getRight() != null) {
            previous = rankingRepository.findAllByDate(datesToCompare.getRight());
        }

        return compareRanking(current, previous);
    }

    private List<RankingDataNew> convertRanking(List<Ranking> rankings) {
        List<RankingDataNew> rankingData = new ArrayList<>(rankings.size());
        // sort
        rankings.sort(Comparator.comparing(Ranking::getPoints).reversed());
        Integer rankPos = 1;
        Integer lastPoints = null;
        for (Ranking ranking : rankings) {
            Integer currentPoint = ranking.getPoints();
            if (lastPoints != null && !lastPoints.equals(currentPoint)) {
                rankPos++;
            }
            lastPoints = currentPoint;

            rankingData.add(RankingDataNew.builder()
                    .account(ranking.getAccount())
                    .date(ranking.getDate())
                    .points(ranking.getPoints())
                    .rank(rankPos).build());
        }
        return rankingData;
    }

    private List<RankingDataNew> compareRanking(List<Ranking> current, List<Ranking> previous) {
        List<RankingDataNew> currentRankingData = convertRanking(current);
        if (previous != null && !previous.isEmpty()) {
            List<RankingDataNew> prevRankingData = convertRanking(previous);
            // convert to map for easy access
            Map<Long, RankingDataNew> prevRankLookup = prevRankingData.stream().collect(Collectors.toMap(o -> o.getAccount().getId(), Function.identity()));
            // update previous data
            currentRankingData.forEach(rankingDataNew -> {
                RankingDataNew prevData = prevRankLookup.get(rankingDataNew.getAccount().getId());
                if (prevData != null) {
                    rankingDataNew.setPrevPoints(prevData.getPoints());
                    rankingDataNew.setPrevRank(prevData.getRank());
                }
            });
        }
        return currentRankingData;
    }

    private Pair<Timestamp, Timestamp> getRankingDatesToCompare() {
        List<Timestamp> distinctDates = rankingRepository.findDistinctDates();
        Timestamp firstDate = null;
        Timestamp secondDate = null;
        if (distinctDates != null && !distinctDates.isEmpty()) {
            Iterator<Timestamp> iterator = distinctDates.iterator();
            firstDate = iterator.next();
            if (iterator.hasNext()) {
                secondDate = iterator.next();
            }
        }
        return Pair.of(firstDate, secondDate);
    }

    private List<Ranking> createRanking(LocalDateTime dateTime) {
        Timestamp timestamp = Timestamp.valueOf(dateTime);
        List<Ranking> rankings = new ArrayList<>();
        List<Bet> allBets = betService.getAllBets();
        int qualifierBetsCount = (int) allBets.stream().filter(b -> b.getType().equals(BetType.QUALIFIER)).count();
        int groupMatchesCount = (int) allBets.stream().filter(b -> b.getType().equals(BetType.MATCH) && b.getStageId().equals(Stage.GROUP)).count();
        int knockoutMatchesCount = (int) allBets.stream().filter(b -> b.getType().equals(BetType.MATCH) && !b.getStageId().equals(Stage.GROUP)).count();
        for (Account account : accountRepository.findAll()) {
            List<UserBet> betForAccount = betService.findByUserBetIdAccountId(account.getId());
            RankingData rankingData = resultsService.getRankingForAccount(account, betForAccount, qualifierBetsCount, groupMatchesCount, knockoutMatchesCount);
            rankings.add(Ranking.builder().account(account).date(timestamp).points(rankingData.getTotalPoints()).build());
        }
        return rankingRepository.save(rankings);
    }

    @Async
    public void createRankingAsync(LocalDateTime dateTime) {
        createRanking(dateTime);
    }
}
