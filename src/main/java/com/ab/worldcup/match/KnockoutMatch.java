package com.ab.worldcup.match;

import com.ab.worldcup.knockout.KnockoutTeam;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.team.Team;
import com.ab.worldcup.team.TeamService;
import com.sun.istack.internal.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@EqualsAndHashCode(callSuper = false)
@Immutable
@Table(name = "knockout_match")
public class KnockoutMatch extends Match implements Serializable {

    @Enumerated(EnumType.STRING)
    private KnockoutMatchCode matchCode;

    @Enumerated(EnumType.STRING)
    private Stage stageId;

    @Enumerated(EnumType.STRING)
    private KnockoutTeamCode homeTeamCode;

    @Enumerated(EnumType.STRING)
    private KnockoutTeamCode awayTeamCode;

    @OneToOne
    @PrimaryKeyJoinColumn
    private KnockoutTeam knockoutTeam;

    @Override
    public String toString() {
        return this.kickoff + " Code " + matchCode + " : " + homeTeamCode + " - " + awayTeamCode;
    }

    @Override
    @Nullable
    public Team getHomeTeam() {
        if (knockoutTeam == null) {
            return TeamService.UndeterminedTeam;
        } else {
            return knockoutTeam.getHomeTeam();
        }
    }

    @Override
    @Nullable
    public Team getAwayTeam() {
        if (knockoutTeam == null) {
            return TeamService.UndeterminedTeam;
        } else {
            return knockoutTeam.getAwayTeam();
        }
    }
}
