package com.ab.worldcup.account;

import com.ab.worldcup.config.ApplicationConfig;
import com.google.common.collect.Sets;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log
@Service
@AllArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;

  private final ApplicationConfig applicationConfig;

  private final PasswordEncoder passwordEncoder;

  private final JavaMailSender mailSender;


//  @Override
//  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//    final Account account = accountRepository.findByEmail(email);
//    if (account == null) {
//      throw new UsernameNotFoundException(email);
//    }
//    return new User(
//        email,
//        account.getPassword(),
//        true,
//        true,
//        true,
//        true,
//        Collections.singletonList(new SimpleGrantedAuthority(Role.USER.getAuthority())));
//  }
//
//  private Account signup(Profile userProfile) {
//    Account account = Account.builder()
//        .email(userProfile.getEmail())
//        .password(randomAlphabetic(8))
//        .firstName(userProfile.getFirstName())
//        .lastName(userProfile.getLastName())
//        .fullName(userProfile.getFullName())
//        .displayName(userProfile.getDisplayName())
//        .gender(userProfile.getGender())
//        .location(userProfile.getLocation())
//        .validatedId(userProfile.getValidatedId())
//        .profileImageUrl(userProfile.getProfileImageURL())
//        .providerId(userProfile.getProviderId())
//        .country(userProfile.getCountry())
//        .status(AccountStatus.REGISTER)
//        .language(userProfile.getLanguage()).build();
//    return accountRepository.save(account);
//  }
//
//  public void registerWithProfile(Profile profile) throws EmailAlreadyInUseException {
//    Account account = accountRepository.findByEmail(profile.getEmail());
//    // already exists - check provider
//    if (account != null && !account.getProviderId().equals(profile.getProviderId())) {
//      throw new EmailAlreadyInUseException(account.getEmail());
//    }
//    // not exists - create account
//    if (account == null) {
//      account = signup(profile);
//    }
//    // set in context
//    SecurityContextHolder.getContext().setAuthentication(
//        new UsernamePasswordAuthenticationToken(
//            account.getEmail(),
//            account.getPassword(),
//            getAuthorities(account.getEmail())));
//  }

  public Account findAccountByEmail(String email) {
    return accountRepository.findByEmail(email);
  }

  private Collection<GrantedAuthority> getAuthorities(String email) {
    Set<GrantedAuthority> authorities = Sets.newHashSet(new SimpleGrantedAuthority(Role.USER.getAuthority()));
    if (applicationConfig.getAdminEmails().contains(email)) {
      authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getAuthority()));
    }
    return authorities;
  }

  public Account updateAccountStatus(Long id, AccountStatus status) {
    Account account = accountRepository.getReferenceById(id);
    account.setStatus(status);
    return accountRepository.save(account);
  }

  public List<Account> getAllAccounts() {
    return accountRepository.findAll();
  }

  // ~ ===========   registration

  public void register(Account account, String siteURL) throws UnsupportedEncodingException, MessagingException {
    String encodedPassword = passwordEncoder.encode(account.getPassword());
    account.setPassword(encodedPassword);

    String randomCode = RandomString.make(64);
    account.setVerificationCode(randomCode);
    account.setEnabled(false);

    accountRepository.save(account);

    sendVerificationEmail(account, siteURL);
  }

  private void sendVerificationEmail(Account account, String siteURL)
      throws MessagingException, UnsupportedEncodingException {
    String toAddress = account.getEmail();
    String fromAddress = "your email address";
    String senderName = "your company name";
    String subject = "Please verify your registration";
    String content = "Dear [[name]],<br>"
        + "Please click the link below to verify your registration:<br>"
        + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
        + "Thank you,<br>"
        + "Your company name.";

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);

    helper.setFrom(fromAddress, senderName);
    helper.setTo(toAddress);
    helper.setSubject(subject);

    content = content.replace("[[name]]", account.getFullName());
    String verifyURL = siteURL + "/verify?code=" + account.getVerificationCode();

    content = content.replace("[[URL]]", verifyURL);

    helper.setText(content, true);

    mailSender.send(message);

    log.info("Email has been sent");
  }

  public boolean verify(String verificationCode) {
    Account account = accountRepository.findByVerificationCode(verificationCode);
    if (account == null || account.isEnabled()) {
      return false;
    } else {
      account.setVerificationCode(null);
      account.setEnabled(true);
      accountRepository.save(account);
      return true;
    }
  }
}
