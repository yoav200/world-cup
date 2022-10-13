package com.ab.worldcup.ranking;

import com.ab.worldcup.account.Account;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RankingDataNew {

    private Timestamp date;

    private Account account;

    private Integer rank;

    private Integer points;

    private Integer prevRank;

    private Integer prevPoints;

}
