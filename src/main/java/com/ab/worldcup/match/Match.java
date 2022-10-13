package com.ab.worldcup.match;

import com.ab.worldcup.results.ResultInterface;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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

    public abstract Stage getStageId();

    public Match setResult(ResultInterface result) {
        this.result = result;
        return this;
    }

    public String getLabel() {
        return this.toString();
    }

    public boolean isStarted() {
        return LocalDateTime.now().isAfter(getKickoff().toLocalDateTime());
    }
}