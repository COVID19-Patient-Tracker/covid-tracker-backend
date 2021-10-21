package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.CovidPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CovidPatientRepository extends JpaRepository<CovidPatient, Long> {
}