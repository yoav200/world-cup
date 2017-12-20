package com.ab.worldcup.knockout;

import com.ab.worldcup.match.GroupMatchRepository;
import com.ab.worldcup.match.KnockoutMatch;
import com.ab.worldcup.match.KnockoutMatchRepository;
import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.results.MatchResultRepository;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.team.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ab.worldcup.results.MatchResultType.HOME_TEAM_WON;
import static com.ab.worldcup.team.KnockoutTeamCode.LOSER_SF1;

@Service
public class KnockoutService {

    @Autowired
    GroupMatchRepository groupMatchRepository;
    @Autowired
    KnockoutMatchRepository knockoutMatchRepository;
    @Autowired
    MatchResultRepository matchResultRepository;
    @Autowired
    KnockoutTeamRepository knockoutTeamRepository;

    private final static Logger logger = LoggerFactory.getLogger(KnockoutService.class);

    public Optional<Team> getKnockoutTeamByTeamCode(KnockoutTeamCode teamCode){
        Optional<Team> team = Optional.empty();
        switch(teamCode.getType()){
            case GROUP_QUALIFIER:
                team = getGroupQualifierByTeamCode(teamCode);
                break;
            case KNOCKOUT_MATCH_QULIFIER:
                team = getKnockoutQualifierByTeamCode(teamCode);
        }
        return team;
    }

    private Optional<Team> getKnockoutQualifierByTeamCode(KnockoutTeamCode teamCode) {
        if(!teamCode.getKnockoutMatchCode().isPresent()){
            logger.warn("Wrong Input");
            return Optional.empty();
        }

        KnockoutMatch match = knockoutMatchRepository.findByKnockoutMatchCode(teamCode.getKnockoutMatchCode().get());
        Long matchId = match.getMatchId();
        MatchResult matchResult = matchResultRepository.findOne(matchId);
        KnockoutTeam knockoutMatchTeams = knockoutTeamRepository.findOne(matchId);
        if(matchResult == null){
            logger.debug("trying to calculate knockout match team for match ID " + matchId + " but match hasn't finished yet");
            return Optional.empty();
        }else{
            if (LOSER_SF1.equals(teamCode) || LOSER_SF1.equals(teamCode)){
                // return match loser
                return Optional.of(getKnockMatchLoser(knockoutMatchTeams,matchResult));
            }else{
                // return match winner
                return Optional.of(getKnockMatchWinner(knockoutMatchTeams,matchResult));
            }
        }

    }

    private Optional<Team> getGroupQualifierByTeamCode(KnockoutTeamCode teamCode) {
        // TODO: implement
        return null;
    }

    public Team getKnockMatchWinner(KnockoutTeam matchTeams, MatchResult result){
        return HOME_TEAM_WON.equals(result.getWinner()) ? matchTeams.getHomeTeam() : matchTeams.getAwayTeam();
    }

    public Team getKnockMatchLoser(KnockoutTeam matchTeams, MatchResult result){
        return HOME_TEAM_WON.equals(result.getWinner()) ? matchTeams.getAwayTeam() : matchTeams.getHomeTeam();
    }
}
