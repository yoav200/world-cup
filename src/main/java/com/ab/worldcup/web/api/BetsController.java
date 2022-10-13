package com.ab.worldcup.web.api;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountService;
import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetService;
import com.ab.worldcup.bet.BetStatisticsData;
import com.ab.worldcup.bet.QualifierBetData;
import com.ab.worldcup.bet.UserBet;
import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.group.GroupStanding;
import com.ab.worldcup.match.Stage;
import com.ab.worldcup.results.Qualifier;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.web.model.BetOverviewData;
import com.ab.worldcup.web.model.MatchesData;
import com.ab.worldcup.web.model.UserBetData;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

  @Autowired
  @org.springframework.beans.factory.annotation.Qualifier("UserBetDataValidator")
  private Validator userBetDataValidator;

  @Autowired
  @org.springframework.beans.factory.annotation.Qualifier("QualifiersBetValidator")
  private Validator qualifiersBetValidator;


  @InitBinder("userBetData")
  protected void initUserBinder(WebDataBinder binder) {
    binder.setValidator(userBetDataValidator);
  }

  @InitBinder("qualifierBetData")
  protected void initPaymentBinder(WebDataBinder binder) {
    binder.setValidator(qualifiersBetValidator);
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
      List<UserBet> userBetList = betService.getUserBetForGroup(groupId, account.getId());
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
  @PostMapping(value = "/user/{betId}")
  public UserBet setUserBet(@PathVariable Long betId, @RequestBody @Valid UserBetData userBetData,
      Principal principal) {
    Account account = accountService.findAccountByEmail(principal.getName());
    Assert.isTrue(betId.equals(userBetData.getBetId()), "betId mismatch");
    return betService.updateMatchBet(account, userBetData);
  }

  @ResponseBody
  @DeleteMapping(value = "/user/{betId}")
  public ResponseEntity deleteUserBet(@PathVariable Long betId, Principal principal) {
    Account account = accountService.findAccountByEmail(principal.getName());
    betService.deleteUserBet(account.getId(), betId);
    return ResponseEntity.ok().build();
  }

  @ResponseBody
  @GetMapping(value = "/user/overview")
  public List<BetOverviewData> overview(Principal principal) {
    Account account = accountService.findAccountByEmail(principal.getName());
    return betService.getOverview(account.getId());
  }

  @ResponseBody
  @GetMapping(value = "/user/qualifiers")
  public QualifierBetData getQualifierBets(Principal principal) {
    Account account = accountService.findAccountByEmail(principal.getName());
    return betService.getQualifierBets(account.getId());
  }

  @ResponseBody
  @PostMapping(value = "/user/qualifiers")
  public QualifierBetData setQualifierBets(@RequestBody @Valid QualifierBetData qualifierBetData, Principal principal) {
    Account account = accountService.findAccountByEmail(principal.getName());
    Map<Stage, List<Qualifier>> qualifiersByStage = qualifierBetData.getQualifiersList().stream()
        .collect(Collectors.groupingBy(Qualifier::getStageId));

    betService.setQualifiersBets(account, qualifiersByStage);
    return qualifierBetData;
  }

  @RequestMapping("/statistics/{betId}")
  public BetStatisticsData getBetsStatistics(@PathVariable Long betId) {
    return betService.getBetStats(betId);
  }

}
