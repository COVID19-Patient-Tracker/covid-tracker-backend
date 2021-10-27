package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.dao.RapidAntigenTestDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RapidAntigenTestDAORepository extends JpaRepository<RapidAntigenTestDAO,Integer> {
    List<RapidAntigenTestDAO> findByPatientId(Long patientId);
}
