package com.ab.worldcup.bet;

import com.ab.worldcup.results.Qualifier;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QualifierBetData {
    List<Qualifier> qualifiersList;
}
