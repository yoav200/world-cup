package com.ab.worldcup.account;

import com.ab.worldcup.config.ApplicationConfig;
import com.ab.worldcup.events.UserLoggedInEvent;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CustomOidcUserService extends OidcUserService {

  private final AccountService accountService;
  private final ApplicationConfig applicationConfig;
  private final ApplicationEventPublisher eventPublisher;

  public CustomOidcUserService(AccountService accountService, ApplicationConfig applicationConfig, ApplicationEventPublisher eventPublisher) {
    this.accountService = accountService;
    this.applicationConfig = applicationConfig;
    this.eventPublisher = eventPublisher;
  }

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUser oidcUser = super.loadUser(userRequest);

    String email = oidcUser.getEmail();
    String name = oidcUser.getFullName();

    if (email == null) {
      email = oidcUser.getAttribute("sub");
    }

    log.info("OAuth2 login for user: {} ({})", email, name);

    // Auto-provision account on first login
    Account account = accountService.findOrCreateFromOidc(email, name);

    // Publish login event (coin listener will handle coin logic)
    eventPublisher.publishEvent(new UserLoggedInEvent(account.getId()));

    // Build authorities: every SSO user gets ROLE_USER, admins get ROLE_ADMIN
    Set<GrantedAuthority> authorities = new HashSet<>();
    authorities.add(new SimpleGrantedAuthority(Role.USER.getAuthority()));

    String finalEmail = email;
    boolean isAdmin = applicationConfig.getAdminEmails().stream()
        .anyMatch(adminEmail -> adminEmail.equalsIgnoreCase(finalEmail));
    log.info("Admin check for {}: adminEmails={}, isAdmin={}", email, applicationConfig.getAdminEmails(), isAdmin);
    if (isAdmin) {
      authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getAuthority()));
    }

    // Return OidcUser with email as the name attribute
    return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo(), "email");
  }
}
