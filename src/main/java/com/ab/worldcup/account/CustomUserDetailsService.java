package com.ab.worldcup.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private AccountRepository accountRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Account account = accountRepository.findByEmail(username);
    if (account == null) {
      throw new UsernameNotFoundException("User not found");
    }
    return new CustomUserDetails(account);
  }

}
