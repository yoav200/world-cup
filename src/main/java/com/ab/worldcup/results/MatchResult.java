package com.ab.worldcup.results;

import com.ab.worldcup.team.Team;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MatchResult implements ResultInterface {

    @Id
    private Long matchId;

    private int homeTeamGoals;

    @ManyToOne
    @JoinColumn(name="homeTeam",referencedColumnName="id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name="awayTeam",referencedColumnName="id")
    private Team awayTeam;

    private int awayTeamGoals;

    @Enumerated(EnumType.STRING)
    private MatchResultType winner;

}
