package com.ab.worldcup.match;

import com.ab.worldcup.results.ResultInterface;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

@Immutable
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