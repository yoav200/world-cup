package com.ab.worldcup.signin;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Service
public class AccountConnectionSignUp implements ConnectionSignUp {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public String execute(Connection<?> connection) {
        UserProfile profile = connection.fetchUserProfile();
        Account account = new Account(profile.getUsername(), randomAlphabetic(8), profile.getFirstName(), profile.getLastName(), profile.getEmail());
        accountRepository.save(account);
        return account.getUsername();
    }
}
