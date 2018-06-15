package com.ab.worldcup.ranking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface RankingRepository extends JpaRepository<Ranking, Long> {

    //@Query(value = "SELECT distinct date FROM ranking order by date desc", nativeQuery = true)
    @Query("SELECT DISTINCT date FROM Ranking ORDER order by date desc")
    List<Timestamp> findDistinctDates();

    List<Ranking> findAllByDate(Timestamp date);
}
