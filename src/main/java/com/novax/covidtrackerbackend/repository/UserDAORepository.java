package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.dao.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDAORepository extends JpaRepository<UserDAO, Long>, JpaSpecificationExecutor<UserDAO> {

    Optional<UserDAO> findByNicAndHospitalusersHospital(String nic , Integer hospitalId);
    Optional<UserDAO> findByEmailAndHospitalusersHospital(String email , Integer hospitalId);
    List<UserDAO> findByRoleAndHospitalusersHospital(String role , Integer hospitalId);
}
