package com.ab.worldcup.match;

import com.ab.worldcup.team.KnockoutTeamCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@Immutable
public class KnockoutMatch extends Match{

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
