package com.ab.worldcup.results;

import com.ab.worldcup.match.Stage;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualifierRepository extends JpaRepository<Qualifier, KnockoutTeamCode> {
    Qualifier findByTeamAndStageId(Team team,Stage stageId);
}
