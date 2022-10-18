package com.ab.worldcup.registration;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.registration.token.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/registration")
public class RegistrationController {

  private final RegistrationService registrationService;

  /**
   * Register new user
   *
   * @param request user details
   * @return new account
   */
  @PostMapping
  public Account register(@Valid @RequestBody RegistrationRequest request) {
    return registrationService.register(request);
  }

  /**
   * confirm account email for the first time
   *
   * @param token pre created token for the account
   * @return account after verification
   */
  @GetMapping(path = "confirm")
  public Account confirm(@RequestParam("token") String token) {
    ConfirmationToken confirmationToken = registrationService.confirmToken(token);
    return confirmationToken.getAccount();
  }

  /**
   * look for account by token
   *
   * @param token pre created token
   * @return account if one exist for the token
   */
  @GetMapping(path = "token")
  public Account account(@RequestParam("token") String token) {
    return registrationService.getAccountByToken(token);
  }

  /**
   * send verification email
   *
   * @param email and email for an existing account
   * @return account after sending verification email (activation or update details)
   */
  @GetMapping(path = "email-validation")
  public Account emailValidation(@RequestParam("email") String email) {
    return registrationService.sendValidationEmail(email);
  }

  @PutMapping(path = "change-password")
  public Account changePassword(@Valid @RequestBody RegistrationRequest request) {
    return registrationService.changePassword(request);
  }
}
