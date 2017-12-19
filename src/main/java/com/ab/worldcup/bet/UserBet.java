package com.ab.worldcup.bet;

import com.ab.worldcup.account.Account;
import com.ab.worldcup.results.MatchResultType;
import com.ab.worldcup.team.Team;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserBet {

    @EmbeddedId
    private UserBetId id;

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


}
