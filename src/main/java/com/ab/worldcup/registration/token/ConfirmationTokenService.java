package com.ab.worldcup.registration.token;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final AccountService accountService;

    public ConfirmationToken createConfirmationToken(Account account, int expirationTimeMinutes) {
        return confirmationTokenRepository.save(new ConfirmationToken(account, expirationTimeMinutes));
    }

    public ConfirmationToken reuseConfirmationToken(ConfirmationToken token, int expirationTimeMinutes) {
        return confirmationTokenRepository.save(token.reuse(expirationTimeMinutes));
    }

    @Transactional
    public ConfirmationToken confirmToken(ConfirmationToken confirmationToken) {
        if (StringUtils.isBlank(confirmationToken.getToken())) {
            throw new IllegalStateException("invalid token");
        }
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("token already has been used");
        }

        enableAccount(confirmationToken);

        setConfirmedAt(confirmationToken.getToken());

        return findByToken(confirmationToken.getToken()).orElseThrow();
    }

    public Optional<ConfirmationToken> findByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public Optional<ConfirmationToken> findByAccountId(Long accountId) {
        return confirmationTokenRepository.findByAccountId(accountId);
    }

    private void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    private void enableAccount(ConfirmationToken confirmationToken) {
        if (!confirmationToken.getAccount().getEnabled()) {
            confirmationTokenRepository.enableAccount(confirmationToken.getAccount().getEmail());
        }
    }
}
