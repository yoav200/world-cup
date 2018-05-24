package com.ab.worldcup.knockout;

import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.group.GroupStanding;
import com.ab.worldcup.group.TeamInGroup;
import com.ab.worldcup.match.*;
import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.team.Team;
import com.google.common.collect.Iterables;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.ab.worldcup.team.KnockoutTeamCode.LOSER_SF1;
import static com.ab.worldcup.team.KnockoutTeamCode.LOSER_SF2;

@Log
@Service
public class KnockoutService<T extends ResultInterface> {

    @Autowired
    private KnockoutMatchRepository knockoutMatchRepository;

    @Autowired
    private KnockoutTeamRepository knockoutTeamRepository;

    @Autowired
    private GroupService groupService;

    private Optional<KnockoutTeam> getKnockoutTeamForKnockoutMatch(KnockoutMatch match, List<T> results) {
        Optional<Team> homeTeam = getKnockoutTeamByTeamCode(match.getHomeTeamCode(), results);
        Optional<Team> awayTeam = getKnockoutTeamByTeamCode(match.getAwayTeamCode(), results);

        KnockoutTeam knockoutTeamRecord = null;

        if (homeTeam.isPresent() || awayTeam.isPresent()) {
            knockoutTeamRecord = new KnockoutTeam();
            knockoutTeamRecord.setMatchId(match.getMatchId());
            homeTeam.ifPresent(knockoutTeamRecord::setHomeTeam);
            awayTeam.ifPresent(knockoutTeamRecord::setAwayTeam);
        }
        return Optional.ofNullable(knockoutTeamRecord);
    }

    private Optional<Team> getKnockoutTeamByTeamCode(KnockoutTeamCode teamCode, List<T> results) {
        Optional<Team> team = Optional.empty();
        switch (teamCode.getType()) {
            case GROUP_QUALIFIER:
                team = getGroupQualifierByTeamCode(teamCode, results);
                break;
            case KNOCKOUT_MATCH_QUALIFIER:
                team = getKnockoutQualifierByTeamCode(teamCode, results);
        }
        return team;
    }

    private Optional<Team> getKnockoutQualifierByTeamCode(KnockoutTeamCode teamCode, List<T> results) {
        if (!teamCode.getKnockoutMatchCode().isPresent()) {
            log.log(Level.WARNING, "Wrong Input");
            return Optional.empty();
        }

        KnockoutMatch match = knockoutMatchRepository.findByMatchCode(teamCode.getKnockoutMatchCode().get());
        Long matchId = match.getMatchId();
        Optional<T> matchResult = results.stream().filter(t -> t.getMatchId().equals(matchId)).findFirst();

        if (!matchResult.isPresent()) {
            log.log(Level.FINE, "trying to calculate knockout match team for match ID " + matchId + " but match hasn't finished yet");
            return Optional.empty();
        } else {
            if (LOSER_SF1.equals(teamCode) || LOSER_SF2.equals(teamCode)) {
                // return match loser
                return Optional.of(getKnockMatchLoser(matchResult.get()));
            } else {
                // return match winner
                return Optional.of(matchResult.get().getKnockoutQualifier());
            }
        }
    }

    private Optional<Team> getGroupQualifierByTeamCode(KnockoutTeamCode teamCode, List<T> results) {
        Optional<Group> group = teamCode.getRelevantGroup();
        if (!group.isPresent()) {
            log.log(Level.WARNING, "Wrong Input");
            return Optional.empty();
        }
        if (!groupService.isGroupFinished(group.get(), results)) {
            return Optional.empty();
        } else {
            GroupStanding groupStanding = groupService.getGroupStanding(group.get(), results);
            TreeSet<TeamInGroup<? super ResultInterface>> teamsInGroup = groupStanding.getTeamsInGroup();
            TeamInGroup teamInGroup = Iterables.get(teamsInGroup, teamCode.isGroupWinner() ? 0 : 1);
            return Optional.of(teamInGroup.getTeam());
        }
    }

    private Team getKnockMatchLoser(ResultInterface result) {
        return result.getHomeTeam().equals(result.getKnockoutQualifier()) ? result.getAwayTeam() : result.getHomeTeam();
    }

    @Cacheable("allKnockoutMatchesCache")
    public List<KnockoutMatch> getAllKnockoutMatches() {
        return knockoutMatchRepository.findAll();
    }

    public List<KnockoutMatch> addKnockoutTeamsOnKnockoutMatch(List<KnockoutMatch> knockoutMatchList) {
        List<KnockoutTeam> knockoutTeams = knockoutTeamRepository.findAll();
        Map<Long, KnockoutTeam> knockoutTeamByMatchId = knockoutTeams.stream().collect(Collectors.toMap(KnockoutTeam::getMatchId, Function.identity()));
        knockoutMatchList.forEach(match -> match.setKnockoutTeam(knockoutTeamByMatchId.get(match.getMatchId())));
        return knockoutMatchList;
    }

    private List<KnockoutMatch> getAllByStageId(Stage nextStage) {
        return knockoutMatchRepository.findAllByStageId(nextStage);
    }

    public List<KnockoutTeam> getKnockoutTeamUpdatedByMatch(Match match, List<T> results) {
        List<KnockoutTeam> knockoutTeamList = new ArrayList<>();
        Stage currentStage = match.getStageId();
        boolean groupFinished = false;

        if (Stage.GROUP.equals(currentStage)) {
            Group group = groupService.getGroupIdByMatchId(match.getMatchId());
            groupFinished = groupService.isGroupFinished(group, results);
        }

        if (groupFinished || !Stage.GROUP.equals(currentStage)) {
            List<Stage> nextStages = currentStage.getNextStage();
            for (Stage nextStage : nextStages) {
                List<KnockoutMatch> knockoutMatchesInNextStage = getAllByStageId(nextStage);
                for (KnockoutMatch knockoutMatch : knockoutMatchesInNextStage) {
                    Optional<KnockoutTeam> knockoutTeam = getKnockoutTeamForKnockoutMatch(knockoutMatch, results);
                    knockoutTeam.ifPresent(knockoutTeamList::add);
                }
            }
        }
        return knockoutTeamList;
    }

//    public KnockoutMatch findKnockoutMatch(Long matchId) {
//        return knockoutMatchRepository.findOne(matchId);
//    }

//    private List<KnockoutMatch> findNextEffectedMatches(Match match) {
//        Set<KnockoutTeamCode> nextEffectedStage = new HashSet<>();
//        if (match.getStageId().equals(Stage.GROUP)) {
//            GroupMatch groupMatch = (GroupMatch) match;
//            nextEffectedStage.addAll(KnockoutTeamCode.getNextEffectedStage(groupMatch.getGroupId()));
//        } else {
//            KnockoutMatch knockoutMatch = (KnockoutMatch) match;
//            nextEffectedStage.add(KnockoutTeamCode.getNextEffectedStage(knockoutMatch.getHomeTeamCode()));
//        }
//        return knockoutMatchRepository.findAllByHomeTeamCodeInOrAwayTeamCodeIn(nextEffectedStage, nextEffectedStage);
//    }

//    public boolean isResultsEffected(Match match, List<ResultInterface> results) {
//        Map<Long, List<ResultInterface>> resultsMap = results.stream().collect(Collectors.groupingBy(ResultInterface::getMatchId));
//        List<KnockoutMatch> nextEffectedMatches = findNextEffectedMatches(match);
//        List<Long> effectedMatchesIds = nextEffectedMatches.stream().map(Match::getMatchId).collect(Collectors.toList());
//        return resultsMap.keySet().stream().anyMatch(effectedMatchesIds::contains);
//    }
}
