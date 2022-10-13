package com.ab.worldcup.bet;

import com.ab.worldcup.results.Qualifier;
import java.sql.Timestamp;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QualifierBetData {
    Timestamp lockTime;
    List<Qualifier> qualifiersList;
}
