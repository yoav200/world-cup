package com.ab.worldcup.account;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @JsonIgnore
    @Column(nullable = false, length = 64)
    private String password;

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