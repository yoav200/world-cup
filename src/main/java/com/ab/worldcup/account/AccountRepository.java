package com.ab.worldcup.account;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByEmail(String email);

  @Query("SELECT a FROM Account a WHERE a.enabled = true")
  List<Account> findAllActiveUsers();
}
