package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.RapidAntigenTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RapidAntigenRepository extends CrudRepository<RapidAntigenTest,Integer> {
}
