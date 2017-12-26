package com.ab.worldcup.knockout;

import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.group.TeamInGroup;
import com.ab.worldcup.match.KnockoutMatch;
import com.ab.worldcup.match.KnockoutMatchRepository;
import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.team.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ab.worldcup.results.MatchResultType.HOME_TEAM_WON;
import static com.ab.worldcup.team.KnockoutTeamCode.LOSER_SF1;

@Service
public class KnockoutService <T extends ResultInterface>{

    @Autowired
    KnockoutMatchRepository knockoutMatchRepository;
    @Autowired
    KnockoutTeamRepository knockoutTeamRepository;
    @Autowired
    GroupService groupService;

    private final static Logger logger = LoggerFactory.getLogger(KnockoutService.class);

    public Optional<KnockoutTeam> getKnockoutTeamForKnockoutMatch(KnockoutMatch match, List<T> results){
        Optional<Team> homeTeam = getKnockoutTeamByTeamCode(match.getHomeTeamCode(), results);
        Optional<Team> awayTeam = getKnockoutTeamByTeamCode(match.getAwayTeamCode(), results);

        KnockoutTeam knockoutTeamRecord = knockoutTeamRepository.getOne(match.getMatchId());

        if(knockoutTeamRecord == null && (homeTeam.isPresent() || awayTeam.isPresent())){
            knockoutTeamRecord = new KnockoutTeam();
            knockoutTeamRecord.setMatchId(match);
        }

        if(homeTeam.isPresent()){
            knockoutTeamRecord.setHomeTeam(homeTeam.get());
        }

        if(awayTeam.isPresent()){
            knockoutTeamRecord.setAwayTeam(awayTeam.get());
        }

        return Optional.ofNullable(knockoutTeamRecord);
    }
    public Optional<Team> getKnockoutTeamByTeamCode(KnockoutTeamCode teamCode,List<T> results){
        Optional<Team> team = Optional.empty();
        switch(teamCode.getType()){
            case GROUP_QUALIFIER:
                team = getGroupQualifierByTeamCode(teamCode,results);
                break;
            case KNOCKOUT_MATCH_QULIFIER:
                team = getKnockoutQualifierByTeamCode(teamCode,results);
        }
        return team;
    }

    private Optional<Team> getKnockoutQualifierByTeamCode(KnockoutTeamCode teamCode, List<T> results) {
        if(!teamCode.getKnockoutMatchCode().isPresent()){
            logger.warn("Wrong Input");
            return Optional.empty();
        }

        KnockoutMatch match = knockoutMatchRepository.findByMatchCode(teamCode.getKnockoutMatchCode().get());
        Long matchId = match.getMatchId();
        Optional<T> matchResult = results.stream().filter(t->t.getMatchId().equals(matchId)).findFirst();
        KnockoutTeam knockoutMatchTeams = knockoutTeamRepository.findOne(matchId);
        if(!matchResult.isPresent()){
            logger.debug("trying to calculate knockout match team for match ID " + matchId + " but match hasn't finished yet");
            return Optional.empty();
        }else{
            if (LOSER_SF1.equals(teamCode) || LOSER_SF1.equals(teamCode)){
                // return match loser
                return Optional.of(getKnockMatchLoser(knockoutMatchTeams,matchResult.get()));
            }else{
                // return match winner
                return Optional.of(getKnockMatchWinner(knockoutMatchTeams,matchResult.get()));
            }
        }

    }

    private Optional<Team> getGroupQualifierByTeamCode(KnockoutTeamCode teamCode, List<T> results) {
        Optional<Group> group = teamCode.getRelevantGroup();
        if(!group.isPresent()){
            logger.warn("Wrong Input");
            return Optional.empty();
        }
        if(!groupService.isGroupFinished(group.get(),results)){
            return Optional.empty();
        }else{
            List<TeamInGroup> groupStanding = groupService.getGroupStanding(group.get(), results);
            TeamInGroup teamInGroup = groupStanding.get(teamCode.isGroupWinner() ? 1 : 2);
            return Optional.of(teamInGroup.getTeam());
        }
    }

    public Team getKnockMatchWinner(KnockoutTeam matchTeams, ResultInterface result){
        return HOME_TEAM_WON.equals(result.getWinner()) ? matchTeams.getHomeTeam() : matchTeams.getAwayTeam();
    }

    public Team getKnockMatchLoser(KnockoutTeam matchTeams, ResultInterface result){
        return HOME_TEAM_WON.equals(result.getWinner()) ? matchTeams.getAwayTeam() : matchTeams.getHomeTeam();
    }
}
