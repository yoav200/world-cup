package com.ab.worldcup.web.api;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountService;
import com.ab.worldcup.web.components.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "/identity", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
    @ResponseBody
    public Account connect() {
        Account account = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            account = accountService.findAccountByEmail(authentication.getName());
            if (account != null) {
                for (GrantedAuthority authority : authentication.getAuthorities()) {
                    account.getRoles().add(authority.toString());
                }
            }
        }
        return account;
    }
    
    // ~ ===============================  ADMIN ONLY ==========================

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping(value = "/{idd}", method = RequestMethod.POST)
    public Account updateAccount(@PathVariable String idd, @RequestBody Account account) {
        Long id = EncryptionUtil.decodeId(idd);
        return accountService.updateAccountStatus(id, account.getStatus());
    }
}
