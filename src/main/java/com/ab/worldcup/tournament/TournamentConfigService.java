package com.ab.worldcup.tournament;

import com.ab.worldcup.match.KnockoutMatch;
import com.ab.worldcup.match.KnockoutMatchRepository;
import com.ab.worldcup.match.Stage;
import com.ab.worldcup.results.PointsConfig;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.web.model.TournamentConfig;
import com.ab.worldcup.web.model.TournamentConfig.KnockoutBracketEntry;
import com.ab.worldcup.web.model.TournamentConfig.QualifierCodeEntry;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TournamentConfigService {

    private final KnockoutMatchRepository knockoutMatchRepository;

    @Cacheable("tournamentConfig")
    public TournamentConfig getTournamentConfig() {
        return TournamentConfig.builder()
                .groups(buildGroups())
                .stages(buildStages())
                .qualifierPoints(buildQualifierPoints())
                .knockoutBracket(buildKnockoutBracket())
                .qualifierCodes(buildQualifierCodes())
                .build();
    }

    private List<String> buildGroups() {
        return Arrays.stream(Group.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    private List<String> buildStages() {
        return Arrays.stream(Stage.values())
                .filter(s -> s != Stage.THIRD_PLACE_WINNER && s != Stage.WINNER)
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    private Map<String, Integer> buildQualifierPoints() {
        Map<String, Integer> points = new LinkedHashMap<>();
        for (Stage stage : Stage.values()) {
            if (stage == Stage.GROUP) continue;
            Integer pts = PointsConfig.getQualifierPoints(stage);
            if (pts != null) {
                points.put(stage.name(), pts);
            }
        }
        return points;
    }

    private List<KnockoutBracketEntry> buildKnockoutBracket() {
        List<KnockoutMatch> allKnockoutMatches = knockoutMatchRepository.findAll();
        return allKnockoutMatches.stream()
                .sorted(Comparator.comparing(KnockoutMatch::getMatchId))
                .map(match -> KnockoutBracketEntry.builder()
                        .matchCode(match.getMatchCode().name())
                        .stage(match.getStageId().name())
                        .homeTeamCode(match.getHomeTeamCode().name())
                        .awayTeamCode(match.getAwayTeamCode().name())
                        .matchId(match.getMatchId())
                        .build())
                .collect(Collectors.toList());
    }

    private Map<String, List<QualifierCodeEntry>> buildQualifierCodes() {
        Map<String, List<QualifierCodeEntry>> result = new LinkedHashMap<>();

        for (KnockoutTeamCode code : KnockoutTeamCode.values()) {
            String stageName = code.getStageId().name();
            result.computeIfAbsent(stageName, k -> new ArrayList<>());

            QualifierCodeEntry.QualifierCodeEntryBuilder entryBuilder = QualifierCodeEntry.builder()
                    .code(code.name())
                    .stage(stageName)
                    .type(code.getType().name());

            // Set group for group qualifiers
            Optional<Group> group = code.getRelevantGroup();
            group.ifPresent(g -> entryBuilder.group(g.name()));

            // Set feedsFrom for knockout match qualifiers
            Optional<Pair<KnockoutTeamCode, KnockoutTeamCode>> prevTeams = code.getPrevStageTeams();
            prevTeams.ifPresent(pair -> entryBuilder.feedsFrom(
                    List.of(pair.getLeft().name(), pair.getRight().name())
            ));

            result.get(stageName).add(entryBuilder.build());
        }

        return result;
    }
}
