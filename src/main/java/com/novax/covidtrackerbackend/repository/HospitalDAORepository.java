package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.dao.HospitalDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalDAORepository extends JpaRepository<HospitalDAO,Integer> {

}
