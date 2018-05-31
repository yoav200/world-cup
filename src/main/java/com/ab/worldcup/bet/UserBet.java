package com.ab.worldcup.bet;

import com.ab.worldcup.knockout.KnockoutTeamInterface;
import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.team.Team;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "user_bet")
public class UserBet implements ResultInterface, KnockoutTeamInterface {

    @EmbeddedId
    @JsonIgnore
    private UserBetId userBetId;

    @ManyToOne
    @JoinColumn(name = "homeTeam", referencedColumnName = "id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "awayTeam", referencedColumnName = "id")
    private Team awayTeam;

    private Integer homeTeamGoals;

    private Integer awayTeamGoals;

    @ManyToOne
    @JoinColumn(name = "qualifier", referencedColumnName = "id")
    private Team qualifier;

    private KnockoutTeamCode knockoutTeamCode;

    @Override
    public Long getMatchId() {
        return getUserBetId().getBet().getMatchId();
    }

    @Override
    public Team getKnockoutQualifier() {
        return qualifier;
    }

    private UserBet() {
        // for JPA
    }

    public UserBet(UserBetId userBetId) {
        this.userBetId = userBetId;
    }
}
