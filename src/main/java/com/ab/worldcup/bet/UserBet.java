package com.ab.worldcup.bet;

import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.results.MatchResultType;
import com.ab.worldcup.team.Team;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "UserBet")
public class UserBet implements ResultInterface {

    @EmbeddedId
    private UserBetId userBetId;

    @ManyToOne
    @JoinColumn(name="homeTeam",referencedColumnName="id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name="awayTeam",referencedColumnName="id")
    private Team awayTeam;

    private int homeTeamGoals;

    private int awayTeamGoals;

    @Enumerated(EnumType.STRING)
    private MatchResultType winner;

    @ManyToOne
    @JoinColumn(name="qualifier",referencedColumnName="id")
    private Team qualifier;

    @Override
    public Long getMatchId() {
        return getUserBetId().getBet().getMatchId();
    }


}
