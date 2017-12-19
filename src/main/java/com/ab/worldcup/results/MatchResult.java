package com.ab.worldcup.results;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MatchResult {

    @Id
    private Long matchId;

    private int homeTeamGoals;

    private int awayTeamGoals;

    @Enumerated(EnumType.STRING)
    private MatchResultType winner;

}
