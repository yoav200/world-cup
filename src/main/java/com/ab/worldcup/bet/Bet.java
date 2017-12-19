package com.ab.worldcup.bet;

import com.ab.worldcup.match.Stage;

import javax.persistence.*;

@Entity
public class Bet {

    @Id
    private Long id;

    private String desription;

    @Enumerated(EnumType.STRING)
    private BetType type;

    private Long matchId;

    @Enumerated(EnumType.STRING)
    private Stage stageId;

}
