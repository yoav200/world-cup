package com.ab.worldcup.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Entity
@Immutable
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "Team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private String name;

    private String code;

    private Integer fifaRanking;

    private Integer appearances;

    private Integer titles;

    @Enumerated(EnumType.STRING)
    private Confederation confederation;

    @Enumerated(EnumType.STRING)
    private Group groupId;
}
