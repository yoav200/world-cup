package com.ab.worldcup.bet;

import com.ab.worldcup.results.Qualifier;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class QualifierBetData {
    Timestamp lockTime;
    List<Qualifier> qualifiersList;
}
