package com.ab.worldcup.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByEmail(String email);

    //Account findByEmailAndProviderId(String email, String providerId);

}
