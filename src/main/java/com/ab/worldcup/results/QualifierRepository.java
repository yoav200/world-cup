package com.ab.worldcup.results;

import com.ab.worldcup.match.Stage;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QualifierRepository extends JpaRepository<Qualifier, KnockoutTeamCode> {

    Qualifier findByTeamAndStageId(Team team, Stage stageId);

    List<Qualifier> findByStageId(Stage stageId);
}
