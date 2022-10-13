package com.ab.worldcup.results;

import com.ab.worldcup.match.Stage;
import com.ab.worldcup.team.KnockoutTeamCode;
import com.ab.worldcup.team.Team;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualifierRepository extends JpaRepository<Qualifier, KnockoutTeamCode> {

    Qualifier findByTeamAndStageId(Team team, Stage stageId);

    List<Qualifier> findByStageId(Stage stageId);
}
