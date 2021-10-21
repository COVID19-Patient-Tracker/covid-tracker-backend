package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.dao.RapidAntigenTestDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RapidAntigenTestDAORepository extends JpaRepository<RapidAntigenTestDAO,Integer> {

}
