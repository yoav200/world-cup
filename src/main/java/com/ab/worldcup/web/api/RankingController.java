package com.ab.worldcup.web.api;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountRepository;
import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.bet.BetType;
import com.ab.worldcup.bet.UserBet;
import com.ab.worldcup.match.Stage;
import com.ab.worldcup.results.ResultsService;
import com.ab.worldcup.web.model.RankingData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    @Autowired
    private ResultsService resultsService;

    @Autowired
    private BetService betService;

    @Autowired
    private AccountRepository accountRepository;


    @RequestMapping("/")
    public List<RankingData> getLeaderboard() {
        List<RankingData> rankingDataList = new ArrayList<>();
        List<Bet> allBets = betService.getAllBets();
        int qualifierBetsCount = (int)allBets.stream().filter(b-> b.getType().equals(BetType.QUALIFIER)).count();
        int groupMatchesCount = (int)allBets.stream().filter(b-> b.getType().equals(BetType.MATCH) && b.getStageId().equals(Stage.GROUP)).count();
        int knockoutMatchesCount = (int)allBets.stream().filter(b-> b.getType().equals(BetType.MATCH) && !b.getStageId().equals(Stage.GROUP)).count();

        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            List<UserBet> betForAccount = betService.findByUserBetIdAccountId(account.getId());
            RankingData rankingForAccount = resultsService.getRankingForAccount(account, betForAccount, qualifierBetsCount,groupMatchesCount,knockoutMatchesCount);
            rankingDataList.add(rankingForAccount);
        }
        rankingDataList.sort((o1, o2) -> o1.getTotalPoints() > o2.getTotalPoints() ? -1 : 1);
        return rankingDataList;
    }
}
