package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.PCRTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PCRTestRepository extends CrudRepository<PCRTest,Integer> {
}
