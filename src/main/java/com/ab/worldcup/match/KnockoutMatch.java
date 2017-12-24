package com.ab.worldcup.match;

import com.ab.worldcup.team.KnockoutTeamCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Entity
@Getter
@EqualsAndHashCode(callSuper = false)
@Immutable
public class KnockoutMatch extends Match implements Serializable{

    @Enumerated(EnumType.STRING)
    private KnockoutMatchCode matchCode;

    @Enumerated(EnumType.STRING)
    private Stage stageId;

    @Enumerated(EnumType.STRING)
    private KnockoutTeamCode homeTeamCode;

    @Enumerated(EnumType.STRING)
    private KnockoutTeamCode awayTeamCode;

    @Override
    public String toString() {
        return this.kickoff + " Code " + matchCode + " : " + homeTeamCode +  " - " + awayTeamCode;
    }
}
