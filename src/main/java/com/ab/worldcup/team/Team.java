package com.ab.worldcup.team;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;

@Entity
@Immutable
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"name", "code"})
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String code;

    @Column(name = "iso_code")
    private String isoCode;

    private Integer fifaRanking;

    private Integer appearances;

    private Integer titles;

    @Enumerated(EnumType.STRING)
    private Confederation confederation;

    @Enumerated(EnumType.STRING)
    private Group groupId;

}
