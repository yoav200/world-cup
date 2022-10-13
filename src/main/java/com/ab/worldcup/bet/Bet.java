package com.ab.worldcup.bet;

import com.ab.worldcup.match.Stage;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

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
