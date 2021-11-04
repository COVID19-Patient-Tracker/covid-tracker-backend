package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.Hospital;
import com.novax.covidtrackerbackend.model.HospitalVisitHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends CrudRepository<Hospital,Integer> {
    @Query(value = "SELECT * FROM hospital WHERE hospital_id = :hospital_id", nativeQuery = true)
    public Hospital findNewestRecordByHospitalId(@Param("hospital_id") int hospital_id);

}
