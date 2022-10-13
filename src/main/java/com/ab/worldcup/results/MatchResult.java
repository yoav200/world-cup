package com.ab.worldcup.results;

import static com.ab.worldcup.knockout.KnockoutMatchQualifier.HOME_TEAM;

import com.ab.worldcup.knockout.KnockoutMatchQualifier;
import com.ab.worldcup.team.Team;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@ToString
@EqualsAndHashCode(of = {"matchId"})
@Table(name = "match_result")
@Data
public class MatchResult implements ResultInterface {

    @Id
    private Long matchId;

    private Integer homeTeamGoals;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "homeTeam", referencedColumnName = "id")
    private Team homeTeam;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "awayTeam", referencedColumnName = "id")
    private Team awayTeam;

    private Integer awayTeamGoals;

    // for cases when knockout match ends up with a tie, and the qualifier is set after penalties.
    // WILL BE NULL FOR GROUP MATCHES
    private KnockoutMatchQualifier matchQualifier;

    @Override
    @JsonIgnore
    public Team getKnockoutQualifier() {
        return HOME_TEAM.equals(getMatchQualifier()) ? getHomeTeam() : getAwayTeam();
    }
}
