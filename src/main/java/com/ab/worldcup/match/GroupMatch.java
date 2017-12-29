package com.ab.worldcup.match;

import com.ab.worldcup.results.ResultInterface;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.Team;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Entity
@Getter
@EqualsAndHashCode(callSuper = false)
@Immutable
@Table(name = "group_match")
public class GroupMatch extends Match {

    @ManyToOne
    @JoinColumn(name = "homeTeam", referencedColumnName = "id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "awayTeam", referencedColumnName = "id")
    private Team awayTeam;

    @Enumerated(EnumType.STRING)
    private Group groupId;

    @Setter
    @Transient
    private ResultInterface result;

    @Override
    public String toString() {
        return this.kickoff + " Group " + groupId.name() + " - " + homeTeam.getName() + " - " + awayTeam.getName();
    }
}
