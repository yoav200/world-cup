package com.ab.worldcup.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken save(ConfirmationToken confirmationToken) {
        return confirmationTokenRepository.save(confirmationToken);
    }

    @Transactional
    public ConfirmationToken confirmToken(ConfirmationToken confirmationToken) {
        confirmationToken.validate();

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
