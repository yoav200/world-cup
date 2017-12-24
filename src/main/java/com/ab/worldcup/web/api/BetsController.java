package com.ab.worldcup.web.api;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountService;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.bet.UserBet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/bets")
public class BetsController {

    @Autowired
    private BetService betService;

    @Autowired
    private AccountService accountService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping("/mine")
    public List<UserBet> getBets(Principal principal) {
        Account account = accountService.findAccountByEmail(principal.getName());
        return betService.findByUserBetIdAccountId(account.getId());
    }
}
