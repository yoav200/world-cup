package com.ab.worldcup.match;

import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.knockout.KnockoutService;
import com.ab.worldcup.knockout.KnockoutTeam;
import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.results.Qualifier;
import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.results.ResultsService;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.web.model.MatchResultData;
import com.ab.worldcup.web.model.MatchesData;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.collections4.ListUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;


@Log
@Service
@AllArgsConstructor
public class MatchService {

  private final KnockoutMatchRepository knockoutMatchRepository;

  private final GroupMatchRepository groupMatchRepository;

  private final GroupService groupService;

  private final KnockoutService knockoutService;

  private final ResultsService resultsService;


  /**
   * Call this method only after persisting the result of <code>match</code> this method will: 1. update the relevant
   * KnockoutTeam record if needed 2. will add a record to qualifier table if needed
   */
  @SuppressWarnings("unchecked")
  @CacheEvict(cacheNames = {"allKnockoutMatchesCache", "allMatchResultsCache", "allQualifiersCache"}, allEntries = true)
  public void onMatchFinish(Match match) {
    List<MatchResult> matchResults = resultsService.getAllMatchResults();
    List<KnockoutTeam> knockoutTeamUpdatedByMatch = knockoutService.getKnockoutTeamUpdatedByMatch(match, matchResults);
    for (KnockoutTeam teamUpdatedByMatch : knockoutTeamUpdatedByMatch) {
      KnockoutMatch knockoutMatch = knockoutMatchRepository.findById(teamUpdatedByMatch.getMatchId()).orElseThrow();
      if (teamUpdatedByMatch.getHomeTeam() != null) {
        Qualifier qualifier = Qualifier.builder()
            .team(teamUpdatedByMatch.getHomeTeam())
            .stageId(knockoutMatch.getStageId())
            .knockoutTeamCode(knockoutMatch.getHomeTeamCode())
            .build();
        resultsService.saveQualifier(qualifier);
      }
      if (teamUpdatedByMatch.getAwayTeam() != null) {
        Qualifier qualifier = Qualifier.builder()
            .team(teamUpdatedByMatch.getAwayTeam())
            .stageId(knockoutMatch.getStageId())
            .knockoutTeamCode(knockoutMatch.getAwayTeamCode())
            .build();
        resultsService.saveQualifier(qualifier);
      }
      resultsService.saveKnockoutTeam(teamUpdatedByMatch);
    }

    if (match.getStageId().equals(Stage.FINAL) || match.getStageId().equals(Stage.THIRD_PLACE)) {
      Qualifier qualifier = Qualifier.builder()
          .team(match.getResult().getKnockoutQualifier())
          .stageId(match.getStageId().getNextStage().get(0))
          .knockoutTeamCode(match.getStageId().equals(Stage.FINAL) ? KnockoutTeamCode.WINNER_FINAL
              : KnockoutTeamCode.WINNER_THIRD_PLACE)
          .build();
      resultsService.saveQualifier(qualifier);
    }
  }

  public Match updateMatchResult(Long matchId, MatchResultData matchResult) {
    // get related match
    Match match = getMatchById(matchId);

    MatchResult result = resultsService.findMatchResultByMatchId(match.getMatchId());
    if (result == null) {
      result = new MatchResult();
      result.setMatchId(match.getMatchId());
    }
    // set goals
    result.setHomeTeamGoals(matchResult.getHomeTeamGoals());
    result.setAwayTeamGoals(matchResult.getAwayTeamGoals());
    // knockout match
    if (!Stage.GROUP.equals(match.getStageId())) {
      result.setHomeTeam(groupService.findTeamByCode(matchResult.getHomeTeamCode()));
      result.setAwayTeam(groupService.findTeamByCode(matchResult.getAwayTeamCode()));
      result.setMatchQualifier(matchResult.getMatchQualifier());
    } else {
      result.setHomeTeam(match.getHomeTeam());
      result.setAwayTeam(match.getAwayTeam());
    }
    // save the result
    resultsService.save(result);

    return getMatchById(match.getMatchId()).setResult(result);
  }

  public Match getMatchById(Long matchId) {
    return groupMatchRepository.findById(matchId)
        .map(Match.class::cast)
        .orElseGet(() -> knockoutMatchRepository.findById(matchId).orElseThrow());
  }

  @SuppressWarnings("unchecked")
  public MatchesData getMatchesData(List<? extends ResultInterface> allResults) {
    // filter match results only
    List<? extends ResultInterface> matchResults = allResults.stream().filter(r -> r.getMatchId() != null)
        .collect(Collectors.toList());
    List<GroupMatch> allGroupMatches = addResultsToMatches(groupService.getAllGroupMatches(), matchResults);
    allGroupMatches.sort(Comparator.comparing(o -> o.matchId));

    List knockoutMatchList = addResultsToMatches(knockoutService.getAllKnockoutMatches(), matchResults);
    List<KnockoutMatch> allKnockoutMatch = knockoutService.addKnockoutTeamsOnKnockoutMatch(knockoutMatchList);
    allKnockoutMatch.sort(Comparator.comparing(o -> o.matchId));

    List<Qualifier> allQualifiers = resultsService.getAllQualifiers();
    Map<Stage, List<Qualifier>> map = allQualifiers.stream().collect(Collectors.groupingBy(Qualifier::getStageId));
    return MatchesData.builder().firstStage(allGroupMatches).secondStage(allKnockoutMatch).qualifiers(map).build();
  }

  public <T extends Match> List<T> addResultsToMatches(List<T> matches, List<? extends ResultInterface> results) {
    Map<Long, ResultInterface> resultMap = results.stream()
        .collect(Collectors.toMap(ResultInterface::getMatchId, Function.identity()));
    matches.forEach(match -> match.setResult(resultMap.get(match.getMatchId())));
    return matches;
  }

  @SuppressWarnings("unchecked")
  public List<? extends Match> getAllMatches() {
    return ListUtils.union(groupService.getAllGroupMatches(), knockoutService.getAllKnockoutMatches());
  }
}
