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

    @Query(value = "SELECT EXISTS (SELECT * FROM User WHERE email = :email)", nativeQuery = true)
    public boolean isUserExist(@Param("email") String email);

}