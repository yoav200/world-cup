package com.ab.worldcup.match;

import com.ab.worldcup.knockout.KnockoutTeamInterface;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.team.Team;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;
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

    @Transient
    private KnockoutTeamInterface knockoutTeam;

    @Override
    public String toString() {
        return this.kickoff + ",  " + matchCode + ", "
                + ((getHomeTeam() == null) ? homeTeamCode : getHomeTeam().getName())
                + " : "
                + ((getAwayTeam() == null) ? awayTeamCode : getAwayTeam().getName());
    }

    @Override
    public Team getHomeTeam() {
        return Optional.ofNullable(knockoutTeam).map(KnockoutTeamInterface::getHomeTeam).orElse(null);
    }

    @Override
    public Team getAwayTeam() {
        return Optional.ofNullable(knockoutTeam).map(KnockoutTeamInterface::getAwayTeam).orElse(null);
    }

    public void setKnockoutTeam(KnockoutTeamInterface knockoutTeam){
        this.knockoutTeam = knockoutTeam;
    }

    public boolean isReady() {
        return (getHomeTeam() != null) && (getAwayTeam() != null);
    }
}
