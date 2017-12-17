package com.ab.worldcup.match;

import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.Team;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@Immutable
public class GroupMatch extends Match{
    @ManyToOne
    private Team homeTeam;

    @ManyToOne
    private Team awayTeam;

    @Enumerated(EnumType.STRING)
    private Group groupId;

}
