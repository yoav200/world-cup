package com.ab.worldcup.account;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class AccountService {

  private static final String USER_NOT_FOUND_MSG = "user with email %s not found";

  private final AccountRepository accountRepository;

  public Account findAccountByEmail(String email) {
    return accountRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
  }

  public Account findOrCreateFromOidc(String email, String name) {
    return accountRepository.findByEmail(email).orElseGet(() -> {
      String firstName = name;
      String lastName = "";
      if (name != null && name.contains(" ")) {
        firstName = name.substring(0, name.indexOf(' '));
        lastName = name.substring(name.indexOf(' ') + 1);
      }
      Account account = Account.builder()
          .email(email)
          .firstName(firstName)
          .lastName(lastName != null ? lastName : "")
          .enabled(true)
          .locked(false)
          .status(AccountStatus.ACTIVE)
          .build();
      log.info("Auto-provisioning new account for SSO user: {}", email);
      return accountRepository.save(account);
    });
  }

  public Account updateAccountStatus(Long id, AccountStatus status) {
    return accountRepository.findById(id).map(a -> {
      a.setStatus(status);
      return accountRepository.save(a);
    }).orElseThrow();
  }

  public List<Account> getAllAccounts() {
    return accountRepository.findAll();
  }
}
