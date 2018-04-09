package com.ab.worldcup.web.api;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountService;
import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.bet.UserBet;
import com.ab.worldcup.bet.UserBetId;
import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.group.GroupStanding;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.web.model.MatchesData;
import com.ab.worldcup.web.model.UserBetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
@RequestMapping("/api/bets")
public class BetsController {

    @Autowired
    private BetService betService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private AccountService accountService;


    @InitBinder("userBet")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new UserBetValidator());
    }

    @RequestMapping("/")
    public List<Bet> getAllBets() {
        return betService.getAllBets();
    }

    @RequestMapping("/groups")
    public List<GroupStanding> getAllGroupStanding(Principal principal) {
        List<GroupStanding> list = new ArrayList<>();
        Account account = accountService.findAccountByEmail(principal.getName());
        for (Group groupId : Group.values()) {
            List<UserBet> userBetList = betService.getUserBetForGroup(groupId,account.getId());
            GroupStanding groupStanding = groupService.getGroupStanding(groupId, userBetList);
            list.add(groupStanding);
        }
        return list;
    }


    @RequestMapping("/data")
    public MatchesData getMatchesData(Principal principal) {
        Account account = accountService.findAccountByEmail(principal.getName());
        return betService.getMatchesData(account.getId());
    }


    @RequestMapping("/user/")
    public List<UserBet> getUserBets(Principal principal) {
        Account account = accountService.findAccountByEmail(principal.getName());
        return betService.findByUserBetIdAccountId(account.getId());
    }


    @ResponseBody
    @RequestMapping(value = "/user/{betId}", method = RequestMethod.POST)
    public UserBet setUserBet(@PathVariable Long betId, @RequestBody UserBet userBet, Principal principal) {
        Account account = accountService.findAccountByEmail(principal.getName());
        Bet bet = betService.getBetById(betId);

        userBet.setUserBetId(new UserBetId(account, bet));
        return betService.setUserBet(userBet);
    }
}
