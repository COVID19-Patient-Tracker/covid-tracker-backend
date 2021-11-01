package com.novax.covidtrackerbackend.repository;

import java.util.Optional;

import com.novax.covidtrackerbackend.model.User;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "FROM User WHERE email = :email")
    public Optional<User> getUserByEmail(@Param("email") String email);

    @Query(value = "call add_user(:email,:role,:nic ,:password,:first_name,:last_name,:hospital_id);", nativeQuery = true)
    public Optional<User> addUser(
            @Param("email") String email,
            @Param("role") String role,
            @Param("nic") String nic,
            @Param("password") String password,
            @Param("first_name") String first_name,
            @Param("last_name") String last_name,
            @Param("hospital_id") Integer hospital_id
    );

    @Query(value = "SELECT EXISTS (SELECT * FROM User WHERE email = :email)", nativeQuery = true)
    public Integer isUserExist(@Param("email") String email);

    @Query(value = "call patient_signup(:patient_id,:email,:nic ,:password);", nativeQuery = true)
    public Optional<User> registerPatient(
            @Param("patient_id") Long patient_id,
            @Param("email") String email,
            @Param("nic") String nic,
            @Param("password") String password
    );

}