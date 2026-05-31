package com.ab.worldcup.league;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountRepository;
import com.ab.worldcup.events.UserLoggedInEvent;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class LeagueEventListener {

  private final LeagueService leagueService;
  private final AccountRepository accountRepository;

  @EventListener
  public void onUserLoggedIn(UserLoggedInEvent event) {
    accountRepository.findById(event.accountId()).ifPresent(account -> {
      leagueService.linkAccountByEmail(account);
    });
  }
}
