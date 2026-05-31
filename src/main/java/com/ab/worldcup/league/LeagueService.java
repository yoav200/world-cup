package com.ab.worldcup.league;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.account.AccountRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@AllArgsConstructor
public class LeagueService {

  private static final int MAX_LEAGUES_PER_USER = 2;

  private final LeagueRepository leagueRepository;
  private final LeagueMembershipRepository membershipRepository;
  private final AccountRepository accountRepository;

  // ── Create ────────────────────────────────────────────────────────────

  @Transactional
  public League createLeague(String name, Long creatorAccountId) {
    long count = leagueRepository.countByCreatedById(creatorAccountId);
    if (count >= MAX_LEAGUES_PER_USER) {
      throw new IllegalStateException("Cannot create more than " + MAX_LEAGUES_PER_USER + " leagues");
    }
    Account creator = accountRepository.findById(creatorAccountId)
        .orElseThrow(() -> new IllegalArgumentException("Account not found"));

    League league = League.builder()
        .name(name)
        .createdBy(creator)
        .build();
    league = leagueRepository.save(league);

    // Creator is automatically a member
    addMember(league.getId(), creator.getEmail(), creatorAccountId);
    return league;
  }

  // ── Update ────────────────────────────────────────────────────────────

  @Transactional
  public League updateLeagueName(Long leagueId, String newName, Long requesterId) {
    League league = getLeagueIfOwner(leagueId, requesterId);
    league.setName(newName);
    return leagueRepository.save(league);
  }

  // ── Delete ────────────────────────────────────────────────────────────

  @Transactional
  public void deleteLeague(Long leagueId, Long requesterId) {
    League league = getLeagueIfOwner(leagueId, requesterId);
    List<LeagueMembership> memberships = membershipRepository.findByLeagueId(leagueId);
    membershipRepository.deleteAll(memberships);
    leagueRepository.delete(league);
  }

  // ── Members ───────────────────────────────────────────────────────────

  @Transactional
  public LeagueMembership addMember(Long leagueId, String email, Long requesterId) {
    League league = getLeagueIfOwner(leagueId, requesterId);

    // Check if already a member
    Optional<LeagueMembership> existing = membershipRepository.findByLeagueIdAndEmail(leagueId, email);
    if (existing.isPresent()) {
      return existing.get();
    }

    // Try to link to existing account
    Optional<Account> account = accountRepository.findByEmail(email);

    LeagueMembership membership = LeagueMembership.builder()
        .league(league)
        .email(email)
        .account(account.orElse(null))
        .build();

    return membershipRepository.save(membership);
  }

  @Transactional
  public void removeMember(Long leagueId, Long membershipId, Long requesterId) {
    getLeagueIfOwner(leagueId, requesterId);
    LeagueMembership membership = membershipRepository.findById(membershipId)
        .orElseThrow(() -> new IllegalArgumentException("Membership not found"));
    if (!membership.getLeague().getId().equals(leagueId)) {
      throw new IllegalArgumentException("Membership does not belong to this league");
    }
    // Creator cannot remove themselves
    if (membership.getEmail().equals(membership.getLeague().getCreatedBy().getEmail())) {
      throw new IllegalStateException("Cannot remove the league creator");
    }
    membershipRepository.delete(membership);
  }

  // ── Queries ───────────────────────────────────────────────────────────

  public List<League> getMyLeagues(Long accountId) {
    // Leagues the user is a member of (not just created)
    return membershipRepository.findByAccountId(accountId).stream()
        .map(LeagueMembership::getLeague)
        .toList();
  }

  public List<League> getLeaguesCreatedBy(Long accountId) {
    return leagueRepository.findByCreatedById(accountId);
  }

  public List<LeagueMembership> getMembers(Long leagueId) {
    return membershipRepository.findByLeagueId(leagueId);
  }

  public List<Long> getLinkedAccountIds(Long leagueId) {
    return membershipRepository.findLinkedAccountIdsByLeagueId(leagueId);
  }

  // ── Auto-link on login ────────────────────────────────────────────────

  @Transactional
  public void linkAccountByEmail(Account account) {
    List<LeagueMembership> unlinked = membershipRepository.findUnlinkedByEmail(account.getEmail());
    for (LeagueMembership membership : unlinked) {
      membership.setAccount(account);
      membershipRepository.save(membership);
      log.info("Auto-linked account {} to league {} via email {}",
          account.getId(), membership.getLeague().getId(), account.getEmail());
    }
  }

  // ── Helpers ───────────────────────────────────────────────────────────

  private League getLeagueIfOwner(Long leagueId, Long requesterId) {
    League league = leagueRepository.findById(leagueId)
        .orElseThrow(() -> new IllegalArgumentException("League not found"));
    if (!league.getCreatedBy().getId().equals(requesterId)) {
      throw new SecurityException("Only the league creator can perform this action");
    }
    return league;
  }
}
