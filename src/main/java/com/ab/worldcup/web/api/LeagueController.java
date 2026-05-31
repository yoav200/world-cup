package com.ab.worldcup.web.api;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountRepository;
import com.ab.worldcup.league.League;
import com.ab.worldcup.league.LeagueMembership;
import com.ab.worldcup.league.LeagueService;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/leagues")
public class LeagueController {

  private final LeagueService leagueService;
  private final AccountRepository accountRepository;

  @GetMapping
  public List<League> getMyLeagues(@AuthenticationPrincipal OidcUser oidcUser) {
    Account account = getAccount(oidcUser);
    return leagueService.getMyLeagues(account.getId());
  }

  @GetMapping("/created")
  public List<League> getLeaguesCreatedByMe(@AuthenticationPrincipal OidcUser oidcUser) {
    Account account = getAccount(oidcUser);
    return leagueService.getLeaguesCreatedBy(account.getId());
  }

  @PostMapping
  public ResponseEntity<League> createLeague(@AuthenticationPrincipal OidcUser oidcUser,
      @RequestBody Map<String, String> body) {
    Account account = getAccount(oidcUser);
    String name = body.get("name");
    if (name == null || name.isBlank()) {
      return ResponseEntity.badRequest().build();
    }
    try {
      League league = leagueService.createLeague(name.trim(), account.getId());
      return ResponseEntity.ok(league);
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("/{leagueId}")
  public ResponseEntity<League> updateLeague(@AuthenticationPrincipal OidcUser oidcUser,
      @PathVariable Long leagueId, @RequestBody Map<String, String> body) {
    Account account = getAccount(oidcUser);
    String name = body.get("name");
    if (name == null || name.isBlank()) {
      return ResponseEntity.badRequest().build();
    }
    try {
      League league = leagueService.updateLeagueName(leagueId, name.trim(), account.getId());
      return ResponseEntity.ok(league);
    } catch (SecurityException e) {
      return ResponseEntity.status(403).build();
    }
  }

  @DeleteMapping("/{leagueId}")
  public ResponseEntity<Void> deleteLeague(@AuthenticationPrincipal OidcUser oidcUser,
      @PathVariable Long leagueId) {
    Account account = getAccount(oidcUser);
    try {
      leagueService.deleteLeague(leagueId, account.getId());
      return ResponseEntity.ok().build();
    } catch (SecurityException e) {
      return ResponseEntity.status(403).build();
    }
  }

  @GetMapping("/{leagueId}/members")
  public List<LeagueMembership> getMembers(@PathVariable Long leagueId) {
    return leagueService.getMembers(leagueId);
  }

  @PostMapping("/{leagueId}/members")
  public ResponseEntity<LeagueMembership> addMember(@AuthenticationPrincipal OidcUser oidcUser,
      @PathVariable Long leagueId, @RequestBody Map<String, String> body) {
    Account account = getAccount(oidcUser);
    String email = body.get("email");
    if (email == null || email.isBlank()) {
      return ResponseEntity.badRequest().build();
    }
    try {
      LeagueMembership membership = leagueService.addMember(leagueId, email.trim().toLowerCase(), account.getId());
      return ResponseEntity.ok(membership);
    } catch (SecurityException e) {
      return ResponseEntity.status(403).build();
    }
  }

  @DeleteMapping("/{leagueId}/members/{membershipId}")
  public ResponseEntity<Void> removeMember(@AuthenticationPrincipal OidcUser oidcUser,
      @PathVariable Long leagueId, @PathVariable Long membershipId) {
    Account account = getAccount(oidcUser);
    try {
      leagueService.removeMember(leagueId, membershipId, account.getId());
      return ResponseEntity.ok().build();
    } catch (SecurityException e) {
      return ResponseEntity.status(403).build();
    } catch (IllegalStateException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  private Account getAccount(OidcUser oidcUser) {
    String email = oidcUser.getEmail();
    return accountRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalStateException("Account not found for email: " + email));
  }
}
