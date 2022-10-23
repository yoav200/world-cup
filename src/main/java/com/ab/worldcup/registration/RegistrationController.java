package com.ab.worldcup.registration;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.registration.recaptcha.RecaptchaService;
import com.ab.worldcup.registration.token.ConfirmationToken;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/registration")
public class RegistrationController {

  private final RegistrationService registrationService;

  private final RecaptchaService recaptchaService;

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
  @PostMapping(path = "email-validation")
  public Account emailValidation(
      @RequestParam(value = "recaptcha", required = false) String recaptcha,
      @RequestParam("email") String email
  ) {

    log.info("is captcha valid: {}", recaptchaService.isValid(recaptcha));

    return registrationService.sendValidationEmail(email);
  }

  @PutMapping(path = "change-password")
  public Account changePassword(@Valid @RequestBody RegistrationRequest request) {
    return registrationService.changePassword(request);
  }
}
