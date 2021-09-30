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

    @Query(value = "FROM Patient WHERE nic = :nic AND first_name= :first_name AND last_name= :last_name")
    public Optional<Patient> getUserByNic_Fn_Ln(@Param("nic") String nic, @Param("first_name") String first_name, @Param("last_name") String last_name);

    @Query(value = "call add_patient(:nic,:hospital_id,:address ,:gender,:first_name,:last_name,:dob, :age, :contact_no, :is_user, :is_child);", nativeQuery = true)
    public Optional<Patient> addPatient(
            @Param("nic") String nic,
            @Param("hospital_id") Integer hospital_id,
            @Param("address") String address,
            @Param("gender") Character gender,
            @Param("first_name") String first_name,
            @Param("last_name") String last_name,
            @Param("dob") String dob,
            @Param("age") Integer age,
            @Param("contact_no") String contact_no,
            @Param("is_user") Integer is_user,
            @Param("is_child") Integer is_child


    );

    @Query(value = "SELECT EXISTS (SELECT * FROM Patient WHERE nic = :nic AND first_name= :first_name AND last_name= :last_name )", nativeQuery = true)
    public boolean isPatientExist(@Param("nic") String nic, @Param("first_name") String first_name, @Param("last_name") String last_name);
}