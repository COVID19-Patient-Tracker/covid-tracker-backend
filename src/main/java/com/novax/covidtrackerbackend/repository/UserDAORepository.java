package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.dao.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserDAORepository extends JpaRepository<UserDAO, Long>, JpaSpecificationExecutor<UserDAO> {
    Optional<UserDAO> findByNic(String nic);
    Optional<UserDAO> findByEmail(String email);
}
