package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.CovidPatient;
import com.novax.covidtrackerbackend.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CovidPatientRepository extends JpaRepository<CovidPatient, Long> {
    List<CovidPatient> findCovidPatientsByHospital(Hospital hospital);
}