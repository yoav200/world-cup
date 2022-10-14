package com.ab.worldcup.account;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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