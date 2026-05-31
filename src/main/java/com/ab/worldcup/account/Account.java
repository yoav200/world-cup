package com.ab.worldcup.account;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "account")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
@EqualsAndHashCode
public class Account {

  @Id
  @SequenceGenerator(name = "hibernate_sequence", sequenceName = "hibernate_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
  private Long id;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @JsonIgnore
  @UpdateTimestamp
  private LocalDateTime updateDateTime;

  @Column(nullable = false, unique = true, length = 45)
  private String email;

  @Column(name = "first_name", nullable = false, length = 20)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = 20)
  private String lastName;

  @JsonIgnore
  private Boolean locked = false;

  private Boolean enabled = false;

  @Enumerated(EnumType.STRING)
  private AccountStatus status;

  @Transient
  @JsonProperty("roles")
  private final Set<String> roles = new HashSet<>();

  public String getFullName() {
    return this.firstName + " " + this.lastName;
  }
}