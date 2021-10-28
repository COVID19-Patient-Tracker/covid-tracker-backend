package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.dao.PcrTestDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PcrTestDAORepository extends JpaRepository<PcrTestDAO,Integer> {
    List<PcrTestDAO> findByPatientId(Long patientId);
}
