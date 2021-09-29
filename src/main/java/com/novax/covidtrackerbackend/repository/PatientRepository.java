package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {

    @Query(value = "FROM Patient WHERE email = :email")
    public Optional<Patient> getUserByNic_Fn_Ln(@Param("email") String email);

    @Query(value = "call add_patient(:nic,:email,:password ,:first_name,:last_name,:is_child);", nativeQuery = true)
    public Optional<Patient> addPatient(
            @Param("nic") String nic,
            @Param("email") String email,
            @Param("first_name") String first_name,
            @Param("last_name") String last_name,
            @Param("password") String password,
            @Param("is_child") Integer is_child


    );

    @Query(value = "SELECT EXISTS (SELECT * FROM Patient WHERE email = :email )", nativeQuery = true)
    public boolean isPatientExist(@Param("email") String email);
}