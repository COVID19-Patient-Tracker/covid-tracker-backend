package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.dao.HospitalUserDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalUserDAORepository extends JpaRepository<HospitalUserDAO,Integer> {
}
