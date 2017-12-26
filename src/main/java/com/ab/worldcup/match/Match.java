package com.ab.worldcup.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Match")
public abstract class Match implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    protected Long matchId;

    protected Timestamp kickoff;

}