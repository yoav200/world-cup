package com.ab.worldcup.team;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"name", "code"})
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
