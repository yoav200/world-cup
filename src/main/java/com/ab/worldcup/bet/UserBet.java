package com.ab.worldcup.bet;

import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.team.Team;
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
public class UserBet implements ResultInterface {

    @EmbeddedId
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

    @Override
    public Long getMatchId() {
        return getUserBetId().getBet().getMatchId();
    }

    @Override
    public Team getKnockoutQualifier() {
        return qualifier;
    }
}
