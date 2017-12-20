package com.ab.worldcup.knockout;

import com.ab.worldcup.match.KnockoutMatch;
import com.ab.worldcup.team.Team;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class KnockoutTeam implements Serializable{

    @Id
    private KnockoutMatch matchId;

    @ManyToOne
    @JoinColumn(name="homeTeam",referencedColumnName="id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name="awayTeam",referencedColumnName="id")
    private Team awayTeam;
}
