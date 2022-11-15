package com.ab.worldcup.registration.token;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.web.components.BadRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

  @Id
  @GeneratedValue
  private Long id;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updateDateTime;

  private int usages = 1;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private LocalDateTime expiresAt;

  private LocalDateTime confirmedAt;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "account_id")
  private Account account;

  public ConfirmationToken(Account account, int expirationTimeMinutes) {
    this.token = UUID.randomUUID().toString();
    this.expiresAt = LocalDateTime.now().plusMinutes(expirationTimeMinutes);
    this.account = account;
  }

  public ConfirmationToken reuse(int expirationTimeMinutes) {
    setToken(UUID.randomUUID().toString());
    setExpiresAt(LocalDateTime.now().plusMinutes(expirationTimeMinutes));
    setConfirmedAt(null);
    updateUsages();
    return this;
  }

  private void updateUsages() {
    this.usages += 1;
  }

  public ConfirmationToken validate() {
    if (StringUtils.isBlank(getToken())) {
      throw new BadRequestException("invalid token");
    }
    if (getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new BadRequestException("token expired");
    }
    if (getConfirmedAt() != null) {
      throw new BadRequestException("token already has been used");
    }
    return this;
  }
}