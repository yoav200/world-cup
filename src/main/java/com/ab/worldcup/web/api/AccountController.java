package com.ab.worldcup.web.api;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountService;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

  // ~ ===============================  Registration ==========================


  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    model.addAttribute("user", new Account());
    return "signup_form";
  }

  @PostMapping("/process_register")
  public String processRegister(Account user, HttpServletRequest request)
      throws UnsupportedEncodingException, MessagingException {
    accountService.register(user, getSiteURL(request));
    return "register_success";
  }


  private String getSiteURL(HttpServletRequest request) {
    String siteURL = request.getRequestURL().toString();
    return siteURL.replace(request.getServletPath(), "");
  }

  @GetMapping("/verify")
  public String verifyUser(@Param("code") String code) {
    if (accountService.verify(code)) {
      return "verify_success";
    } else {
      return "verify_fail";
    }
  }

  // ~ ===============================  ADMIN ONLY ==========================

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping(value = "/")
  @ResponseBody
  public List<Account> getAllAccounts() {
    return accountService.getAllAccounts();
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ResponseBody
  @PostMapping(value = "/{id}")
  public Account updateAccount(@PathVariable Long id, @RequestBody Account account) {
    return accountService.updateAccountStatus(id, account.getStatus());
  }
}
