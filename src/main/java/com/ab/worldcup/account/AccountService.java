package com.ab.worldcup.account;

import com.ab.worldcup.registration.RegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ab.worldcup.account.CustomUserDetailsService.USER_NOT_FOUND_MSG;

@Log4j2
@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public Account updateAccountDetails(Account account, RegistrationRequest request) {
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        return accountRepository.save(account);
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


    // ~ ===========   registration

    public Account createNewAccount(RegistrationRequest request) {
        boolean userExists = accountRepository
                .findByEmail(request.getEmail())
                .isPresent();

        if (userExists) {
            throw new IllegalStateException("email already taken");
        }

        Account account = Account.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .enabled(false)
                .locked(false)
                .build();

        return accountRepository.save(account);
    }
}
