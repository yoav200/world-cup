package com.ab.worldcup.account;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Account")
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

}