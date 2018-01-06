package com.ab.worldcup.web.model;

import com.ab.worldcup.match.Stage;
import com.ab.worldcup.results.MatchResult;
import com.ab.worldcup.team.Team;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class MatchData {

    private Long matchId;

    private Timestamp kickoff;

    private Team homeTeam;

    private Team awayTeam;

    private Stage stage;

    private MatchResult result;

    public String getLabel() {
        return "Stage: " + stage
                + " kickoff: " + this.kickoff
                + " - "
                + ((homeTeam == null) ? "undefined" : homeTeam.getName()) + " - "
                + ((awayTeam == null) ? "undefined" : awayTeam.getName());
    }
}
