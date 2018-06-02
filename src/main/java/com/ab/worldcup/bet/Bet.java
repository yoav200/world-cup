package com.ab.worldcup.bet;

import com.ab.worldcup.match.Stage;
import lombok.Getter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "bet")
public class Bet {

    @Id
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private BetType type;

    private Long matchId;

    @Enumerated(EnumType.STRING)
    private Stage stageId;

    private Timestamp lockTime;

    public boolean isLock() {
        return LocalDateTime.now().isAfter(getLockTime().toLocalDateTime());
    }
}
