package com.ab.worldcup.web.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class TournamentConfig {

    private List<String> groups;

    private List<String> stages;

    private Map<String, Integer> qualifierPoints;

    private List<KnockoutBracketEntry> knockoutBracket;

    private Map<String, List<QualifierCodeEntry>> qualifierCodes;

    @Data
    @Builder
    public static class KnockoutBracketEntry {
        private String matchCode;
        private String stage;
        private String homeTeamCode;
        private String awayTeamCode;
        private Long matchId;
    }

    @Data
    @Builder
    public static class QualifierCodeEntry {
        private String code;
        private String stage;
        private String group;
        private String type;
        private List<String> feedsFrom;
    }
}
