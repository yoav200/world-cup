package com.ab.worldcup.web.api;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountService;
import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.bet.UserBet;
import com.ab.worldcup.match.MatchService;
import com.ab.worldcup.web.model.BetData;
import com.ab.worldcup.web.model.MatchData;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bets")
public class BetsController {

    @Autowired
    private BetService betService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private AccountService accountService;


    @RequestMapping("/")
    public List<BetData> getAllBets() {
        List<BetData> list = Lists.newArrayList();

        List<MatchData> matchesData = matchService.getMatchData();
        List<Bet> bets = betService.getAllBets();
        Map<Long, MatchData> collect = matchesData.stream().collect(Collectors.toMap(MatchData::getMatchId, Function.identity()));

        bets.forEach(bet -> list.add(
                BetData.builder()
                        .id(bet.getId())
                        .type(bet.getType())
                        .description(bet.getDescription())
                        .stageId(bet.getStageId())
                        .lockTime(bet.getLockTime())
                        .match(collect.get(bet.getMatchId()))
                        .build()));

        return list;
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/user/")
    public List<UserBet> getUserBets(Principal principal) {
        Account account = accountService.findAccountByEmail(principal.getName());
        return betService.findByUserBetIdAccountId(account.getId());
    }

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @RequestMapping(value = "/user/{betId}", method = RequestMethod.GET)
//    public UserBet getUserBet(@PathVariable Long betId, Principal principal) {
//        Account account = accountService.findAccountByEmail(principal.getName());
//        return betService.getUserBetsByBet()
//    }


    @ResponseBody
    @RequestMapping(value = "/user/{betId}", method = RequestMethod.POST)
    public UserBet setUserBet(@PathVariable Long betId, @RequestBody UserBet userBet, Principal principal) {
        Account account = accountService.findAccountByEmail(principal.getName());
        return betService.setUserBet(userBet);
    }

}
