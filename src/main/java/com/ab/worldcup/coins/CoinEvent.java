package com.ab.worldcup.coins;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coin_event")
public class CoinEvent {

  @Id
  @SequenceGenerator(name = "coin_event_seq", sequenceName = "hibernate_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coin_event_seq")
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "event_type", nullable = false)
  private CoinEventType eventType;

  @Column(nullable = false)
  private Integer coins;

  @Column(name = "account_id")
  private Long accountId;

  @Column(name = "reference_id")
  private Long referenceId;

  @Builder.Default
  @Column(name = "created_at")
  private LocalDateTime createdAt = LocalDateTime.now();
}
