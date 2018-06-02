package com.ab.worldcup.web.validators;

import com.ab.worldcup.bet.QualifierBetData;
import com.ab.worldcup.config.ApplicationConfig;
import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.results.Qualifier;
import com.ab.worldcup.team.Group;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("QualifiersBetValidator")
public class QualifiersBetValidator implements Validator {

    private final GroupService groupService;

    private final ApplicationConfig applicationConfig;

    @Autowired
    public QualifiersBetValidator(ApplicationConfig applicationConfig, GroupService groupService) {
        this.groupService = groupService;
        this.applicationConfig = applicationConfig;
    }

    public boolean supports(Class<?> clazz) {
        return QualifierBetData.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        QualifierBetData qualifierBetData = (QualifierBetData) target;

        if (LocalDateTime.now().isAfter(applicationConfig.getStartDateTime())) {
            errors.rejectValue("qualifiersList", "", "Cannot update qualifier bets after tournament has started");
        }

        boolean sameTeamAppearTwiceInOneStage = qualifierBetData.getQualifiersList().stream().
                collect(Collectors.groupingBy(t -> Pair.of(t.getTeam(), t.getStageId()))).
                entrySet().stream().anyMatch(t -> t.getValue().size() > 1);

        if (sameTeamAppearTwiceInOneStage) {
            errors.rejectValue("qualifiersList", "", "invalid bet - some teams appears twice in the same stage");
        }

        for (Qualifier qualifier : qualifierBetData.getQualifiersList()) {

            Optional<Group> relevantGroup = qualifier.getKnockoutTeamCode().getRelevantGroup();

            if (relevantGroup.isPresent()) {
                if (!groupService.getTeamsByGroup(relevantGroup.get()).contains(qualifier.getTeam())) {
                    errors.rejectValue("qualifiersList", "", qualifier.getKnockoutTeamCode() + " contains invalid team");
                }
            } else {
                Optional<Pair<KnockoutTeamCode, KnockoutTeamCode>> prevStageTeams = qualifier.getKnockoutTeamCode().getPrevStageTeams();
                if (prevStageTeams.isPresent()) {
                    ImmutableList<KnockoutTeamCode> codes = ImmutableList.of(prevStageTeams.get().getRight(), prevStageTeams.get().getLeft());
                    boolean noneMatch = qualifierBetData.getQualifiersList().stream()
                            .filter(t -> codes.contains(t.getKnockoutTeamCode()))
                            .noneMatch(t -> t.getTeam().equals(qualifier.getTeam()));

                    if (noneMatch) {
                        errors.rejectValue("qualifiersList", "", qualifier.getKnockoutTeamCode() + " contains invalid team");
                    }
                }
            }
        }
    }
}
