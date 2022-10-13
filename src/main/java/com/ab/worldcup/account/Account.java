package com.ab.worldcup.account;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
  @GeneratedValue
  private Long id;

  @Column(nullable = false, unique = true, length = 45)
  private String email;

  @Column(nullable = false, length = 64)
  private String password;

  @Column(name = "first_name", nullable = false, length = 20)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = 20)
  private String lastName;

  @Column(name = "verification_code", length = 64)
  private String verificationCode;

  private boolean enabled;

  @Enumerated(EnumType.STRING)
  private AccountStatus status;

  @Transient
  @JsonProperty("roles")
  private final Set<String> roles = new HashSet<>();

  public String getFullName() {
    return this.firstName + " " + this.lastName;
  }
}