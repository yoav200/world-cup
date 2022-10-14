package com.ab.worldcup.account;

import com.ab.worldcup.registration.RegistrationRequest;
import com.ab.worldcup.registration.token.ConfirmationToken;
import com.ab.worldcup.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.ab.worldcup.account.CustomUserDetailsService.USER_NOT_FOUND_MSG;

@Log4j2
@Service
@AllArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;


    public Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
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
        Account account = Account.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .enabled(false)
                .locked(false)
                .build();

        account.getRoles().add(Role.USER.toString());

        boolean userExists = accountRepository
                .findByEmail(account.getEmail())
                .isPresent();

        if (userExists) {
            // TODO check of attributes are the same and
            // TODO if email not confirmed send confirmation email.

            throw new IllegalStateException("email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(account.getPassword());

        account.setPassword(encodedPassword);

        return accountRepository.save(account);
    }

    public ConfirmationToken createConfirmationToken(Account account, int expirationTimeMinutes) {
        ConfirmationToken confirmationToken = new ConfirmationToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusMinutes(expirationTimeMinutes),
                account
        );

        return confirmationTokenService.saveConfirmationToken(confirmationToken);
    }

    public void enableAccount(String email) {
        accountRepository.enableAccount(email);
    }
}
