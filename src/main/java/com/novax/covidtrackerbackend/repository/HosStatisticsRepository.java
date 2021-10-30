package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.HosStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HosStatisticsRepository extends JpaRepository<HosStatistics,Long> {
    @Query(value = "SELECT * FROM hos_statistics WHERE hospital_id = :hospital_id", nativeQuery = true)
    List<HosStatistics> findByhospital_id(@Param("hospital_id") Long hospital_id);
}
// abcd