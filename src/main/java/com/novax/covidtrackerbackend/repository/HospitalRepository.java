package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.Hospital;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends CrudRepository<Hospital,Integer> {

}
