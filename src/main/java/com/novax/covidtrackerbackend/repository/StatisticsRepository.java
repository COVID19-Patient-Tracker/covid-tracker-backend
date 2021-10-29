package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StatisticsRepository extends JpaRepository<Statistics,Long> {
}
