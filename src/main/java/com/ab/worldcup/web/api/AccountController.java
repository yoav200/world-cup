package com.ab.worldcup.web.api;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountService;
import com.ab.worldcup.registration.RegistrationRequest;
import com.ab.worldcup.web.components.BadRequestException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/identity", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Account connect() {
        Account account = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            account = accountService.findAccountByEmail(authentication.getName());
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                account.getRoles().add(authority.toString());
            }
        }
        return account;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping
    public Account updateAccount(@Valid @RequestBody RegistrationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName().equals(request.getEmail())) {
            Account account = accountService.findAccountByEmail(authentication.getName());
            return accountService.updateAccountDetails(account, request);
        }
        throw new BadRequestException("Unauthorized access");
    }

    // ~ ===============================  ADMIN ONLY ==========================

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/{id}")
    public Account updateAccount(@PathVariable Long id, @RequestBody Account account) {
        return accountService.updateAccountStatus(id, account.getStatus());
    }
}
