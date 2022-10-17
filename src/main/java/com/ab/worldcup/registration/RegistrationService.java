package com.ab.worldcup.registration;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountService;
import com.ab.worldcup.config.ApplicationConfig;
import com.ab.worldcup.email.EmailSender;
import com.ab.worldcup.registration.token.ConfirmationToken;
import com.ab.worldcup.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RegistrationService {

    private static final String loginUrl = "%s/#/login/%s";

    private static final String updateDetailsUrl = "%s/#/join/%s";

    private final ApplicationConfig applicationConfig;

    private final AccountService accountService;

    private final ConfirmationTokenService confirmationTokenService;

    private final EmailSender emailSender;

    public Account register(RegistrationRequest request) {
        Account newAccount = accountService.createNewAccount(request);
        return validateEmail(newAccount.getEmail());
    }

    public Account getAccountByToken(String token) {
        return confirmationTokenService
                .findByToken(token)
                .map(t -> t.validate().getAccount())
                .orElseThrow(() -> new IllegalStateException("token not found"));
    }

    @Transactional
    public ConfirmationToken confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        return confirmationTokenService.confirmToken(confirmationToken);
    }

    public Account validateEmail(String email) {
        Account account = accountService.findAccountByEmail(email);

        final int confirmationTimeoutMinutes = applicationConfig.getConfirmationTimeoutMinutes();
        final String appUrl = applicationConfig.getAppUrl();

        // TODO: 16/10/2022 add validation on confirmationToken to avoid email spamming
        ConfirmationToken confirmationToken = confirmationTokenService.findByAccountId(account.getId())
                .map(token -> token.reuse(confirmationTimeoutMinutes))
                .orElse(new ConfirmationToken(account, confirmationTimeoutMinutes));

        confirmationTokenService.save(confirmationToken);

        if (account.getEnabled()) {
            // account is already enabled, send link to update details
            String link = String.format(updateDetailsUrl, appUrl, confirmationToken.getToken());
            emailSender.send(
                    account.getEmail(),
                    "Verify your email",
                    buildActivationEmail(
                            account.getFirstName(),
                            "Please click on the below link to verify your account and update your details ",
                            "Verify",
                            link,
                            confirmationTimeoutMinutes));
        } else {
            // account is not enabled, send activation email
            String link = String.format(loginUrl, appUrl, confirmationToken.getToken());
            emailSender.send(
                    account.getEmail(),
                    "Verify your email",
                    buildActivationEmail(
                            account.getFirstName(),
                            "Thank you for registering. Please click on the below link to activate your account",
                            "Activate Now",
                            link,
                            confirmationTimeoutMinutes));
        }
        return account;
    }

    public Account changePassword(RegistrationRequest request) {
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(request.getToken())
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (!confirmationToken.getAccount().getEmail().equals(request.getEmail())) {
            throw new IllegalArgumentException("Token not match email");
        }

        ConfirmationToken confirmToken = confirmationTokenService.confirmToken(confirmationToken);

        return accountService.updateAccountDetails(confirmToken.getAccount(), request);
    }

    private String buildActivationEmail(
            String name,
            String message,
            String actionText,
            String link,
            int minutes) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> " + message + " </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">" + actionText + "</a> </p></blockquote>\n Link will expire in " + minutes + " minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
