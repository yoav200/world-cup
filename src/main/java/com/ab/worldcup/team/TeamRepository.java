package com.ab.worldcup.team;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByGroupId(Group group);

    Team findByName(String name);

    Team findByCode(String code);
}
