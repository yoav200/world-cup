package com.ab.worldcup.ranking;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RankingRepository extends JpaRepository<Ranking, Long> {

    @Query("SELECT DISTINCT r.date FROM Ranking r ORDER BY r.date DESC")
    List<Timestamp> findDistinctDates();

    List<Ranking> findAllByDate(Timestamp date);

    void deleteByDate(Timestamp date);
}
