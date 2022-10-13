package com.ab.worldcup.knockout;

import com.ab.worldcup.team.Team;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "knockout_team")
public class KnockoutTeam implements Serializable, KnockoutTeamInterface {

    @Id
    private Long matchId;

    @ManyToOne
    @JoinColumn(name = "homeTeam", referencedColumnName = "id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "awayTeam", referencedColumnName = "id")
    private Team awayTeam;

}
