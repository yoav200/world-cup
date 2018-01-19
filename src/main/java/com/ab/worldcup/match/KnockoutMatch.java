package com.ab.worldcup.match;

import com.ab.worldcup.knockout.KnockoutTeam;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.team.Team;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

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

    @JsonIgnore
    @OneToOne(mappedBy = "knockoutMatch")
    private KnockoutTeam knockoutTeam;

    @Override
    public String toString() {
        return this.kickoff + ",  " + matchCode + ", "
                + ((getHomeTeam() == null) ? homeTeamCode : getHomeTeam().getName())
                + " : "
                + ((getAwayTeam() == null) ? awayTeamCode : getAwayTeam().getName());
    }

    @Override
    public Team getHomeTeam() {
        return Optional.ofNullable(knockoutTeam).map(KnockoutTeam::getHomeTeam).orElse(null);
    }

    @Override
    public Team getAwayTeam() {
        return Optional.ofNullable(knockoutTeam).map(KnockoutTeam::getAwayTeam).orElse(null);
    }

    public boolean isReady() {
        return (getHomeTeam() != null) && (getAwayTeam() != null);
    }
}
