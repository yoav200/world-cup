package com.ab.worldcup.results;

import com.ab.worldcup.team.Team;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
//@EntityListeners({MatchResultEntityListener.class})
@ToString
@EqualsAndHashCode(of = {"matchId", "homeTeam", "awayTeam"})
@Table(name = "match_result")
@Data
public class MatchResult implements ResultInterface {

    @Id
    private Long matchId;

    private int homeTeamGoals;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "homeTeam", referencedColumnName = "id")
    private Team homeTeam;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "awayTeam", referencedColumnName = "id")
    private Team awayTeam;

    private int awayTeamGoals;

}
