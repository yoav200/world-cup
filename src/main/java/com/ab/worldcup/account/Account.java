package com.ab.worldcup.account;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account")
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
@EqualsAndHashCode
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private String fullName;

    private String displayName;

    private String gender;

    @JsonIgnore
    private String location;

    @JsonIgnore
    private String validatedId;

    private String profileImageUrl;

    @JsonIgnore
    private String providerId;

    private String country;

    private String language;

    @Transient
    @JsonProperty("roles")
    private final Set<String> roles = new HashSet<>();

}