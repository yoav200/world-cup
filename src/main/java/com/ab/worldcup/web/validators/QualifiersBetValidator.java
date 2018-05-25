package com.ab.worldcup.web.validators;

import com.ab.worldcup.bet.QualifierBetData;
import com.ab.worldcup.group.GroupService;
import com.ab.worldcup.results.Qualifier;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.team.KnockoutTeamCodeType;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("QualifiersBetValidator")
public class QualifiersBetValidator implements Validator {

    private final GroupService groupService;

    @Autowired
    public QualifiersBetValidator(GroupService groupService) {
        this.groupService = groupService;
    }

    public boolean supports(Class<?> clazz) {
        return QualifierBetData.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        QualifierBetData qualifierBetData = (QualifierBetData) target;

        for (Qualifier qualifier : qualifierBetData.getQualifiersList()) {
            if (qualifier.getKnockoutTeamCode().getType().equals(KnockoutTeamCodeType.GROUP_QUALIFIER)) {
                if (!groupService.getTeamsByGroup(qualifier.getKnockoutTeamCode().getRelevantGroup().get()).contains(qualifier.getTeam())) {
                    errors.reject(qualifier.getKnockoutTeamCode() + "contains invalid team");
                } else {
                    Pair<KnockoutTeamCode, KnockoutTeamCode> prevStageTeams = qualifier.getKnockoutTeamCode().getPrevStageTeams().get();
                    if (!qualifierBetData.getQualifiersList().stream().
                            filter(t -> t.getKnockoutTeamCode().equals(prevStageTeams.getRight()) || t.getKnockoutTeamCode().equals(prevStageTeams.getLeft())).
                            anyMatch(t -> t.getTeam().equals(qualifier.getTeam()))) {
                        errors.reject(qualifier.getKnockoutTeamCode() + "contains invalid team");
                    }
                }
            }
        }

    }
}
