package com.ab.worldcup.team;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByGroupId(Group group);

    Team findByName(String name);

    Team findByCode(String code);
}
