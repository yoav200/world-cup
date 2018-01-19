package com.ab.worldcup.web.model;

import com.ab.worldcup.match.GroupMatch;
import com.ab.worldcup.match.KnockoutMatch;
import com.ab.worldcup.match.Stage;
import com.ab.worldcup.results.Qualifier;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class MatchesData {

    private List<GroupMatch> firstStage;

    private List<KnockoutMatch> secondStage;

    private Map<Stage, List<Qualifier>> qualifiers;
}
