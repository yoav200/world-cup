package com.ab.worldcup.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "team")
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
