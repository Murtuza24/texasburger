package com.egen.texasburger.repositories;

import com.egen.texasburger.models.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Murtuza
 */

public interface ApiStatisticsRepository extends JpaRepository<Statistics, Integer> {

    @Query(value = "Select * from apistatistics where status= ?1", nativeQuery = true)
    List<Statistics> getStatsByStatus(@Param("status") Integer status);

    @Query(value = "Select * from apistatistics where method= ?1", nativeQuery = true)
    List<Statistics> getStatsByMethod(@Param("method") String status);


}

