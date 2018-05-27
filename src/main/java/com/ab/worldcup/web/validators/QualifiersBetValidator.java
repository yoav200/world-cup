package com.ab.worldcup.web.validators;

import com.ab.worldcup.bet.Bet;
import com.ab.worldcup.bet.BetService;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;

@Component("QualifiersBetValidator")
public class QualifiersBetValidator implements Validator {

    private final GroupService groupService;
    private final BetService betService;

    @Autowired
    public QualifiersBetValidator(GroupService groupService, BetService betService) {
        this.groupService = groupService;
        this.betService = betService;
    }

    public boolean supports(Class<?> clazz) {
        return QualifierBetData.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        QualifierBetData qualifierBetData = (QualifierBetData) target;

        Timestamp startOfTournamentTime = betService.getAllBets().stream().min(Comparator.comparing(Bet::getLockTime)).get().getLockTime();

        if (LocalDateTime.now().isAfter(startOfTournamentTime.toLocalDateTime())) {
            errors.rejectValue("betId", "", "Cannot update qualifier bets after tournament has started");
        }

        for (Qualifier qualifier : qualifierBetData.getQualifiersList()) {
            if (qualifier.getKnockoutTeamCode().getType().equals(KnockoutTeamCodeType.GROUP_QUALIFIER)) {
                if (!groupService.getTeamsByGroup(qualifier.getKnockoutTeamCode().getRelevantGroup().get()).contains(qualifier.getTeam())) {
                    errors.rejectValue("qualifiersList", "", qualifier.getKnockoutTeamCode() + "contains invalid team");
                }
            }
            else {
                Pair<KnockoutTeamCode, KnockoutTeamCode> prevStageTeams = qualifier.getKnockoutTeamCode().getPrevStageTeams().get();
                boolean noneMatch = qualifierBetData.getQualifiersList().stream().
                        filter(t -> t.getKnockoutTeamCode().equals(prevStageTeams.getRight()) || t.getKnockoutTeamCode().equals(prevStageTeams.getLeft())).
                        noneMatch(t -> t.getTeam().equals(qualifier.getTeam()));

                if (noneMatch) {
                    errors.rejectValue("qualifiersList", "", qualifier.getKnockoutTeamCode() + "contains invalid team");
                }
            }
        }
    }
}
