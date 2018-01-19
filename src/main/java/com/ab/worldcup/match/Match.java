package com.ab.worldcup.match;

import com.ab.worldcup.results.ResultInterface;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "match")
public abstract class Match implements Serializable, MatchTeamsInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long matchId;

    protected Timestamp kickoff;

    protected MatchStatus status;

    @Transient
    private ResultInterface result;

    public Match setResult(ResultInterface result) {
        this.result = result;
        return this;
    }

    public String getLabel() {
        return this.toString();
    }
}