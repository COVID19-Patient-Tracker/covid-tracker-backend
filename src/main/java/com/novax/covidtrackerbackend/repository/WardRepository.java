package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward,Integer> {
    @Query(value = "SELECT * FROM ward WHERE hospital_id = :hospital_id", nativeQuery = true)
    List<Ward> findByHospitalId(int hospital_id);
}
