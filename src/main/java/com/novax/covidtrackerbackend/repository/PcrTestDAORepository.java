package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.dao.PcrTestDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PcrTestDAORepository extends JpaRepository<PcrTestDAO,Integer> {
}
