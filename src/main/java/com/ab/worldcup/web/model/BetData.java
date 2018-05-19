package com.ab.worldcup.web.model;

import com.ab.worldcup.bet.BetType;
import com.ab.worldcup.match.Match;
import com.ab.worldcup.match.Stage;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class BetData {

    private Long id;

    private Long matchId;

    private BetType type;

    private String description;

    private Stage stageId;

    private Timestamp lockTime;

    private Match match;
}
