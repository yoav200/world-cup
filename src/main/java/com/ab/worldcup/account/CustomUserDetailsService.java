package com.ab.worldcup.account;

import com.ab.worldcup.config.ApplicationConfig;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    public static final String USER_NOT_FOUND_MSG = "user with email %s not found";

    private final AccountRepository accountRepository;

    private final ApplicationConfig applicationConfig;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));

        Set<GrantedAuthority> authorities = new HashSet<>(Set.of(new SimpleGrantedAuthority(Role.USER.getAuthority())));
        if (applicationConfig.getAdminEmails().contains(email)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getAuthority()));
        }

        return new CustomUserDetails(account, authorities);
    }

}
