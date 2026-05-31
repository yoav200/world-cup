package com.ab.worldcup.league;

import com.ab.worldcup.account.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "league_membership")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeagueMembership {

  @Id
  @SequenceGenerator(name = "hibernate_sequence", sequenceName = "hibernate_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "league_id", nullable = false)
  private League league;

  @Column(nullable = false, length = 45)
  private String email;

  @ManyToOne
  @JoinColumn(name = "account_id")
  private Account account;

  @CreationTimestamp
  @Column(name = "joined_at")
  private LocalDateTime joinedAt;
}
