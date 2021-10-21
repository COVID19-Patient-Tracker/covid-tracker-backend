package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.CovidPatient;
import com.novax.covidtrackerbackend.repository.CovidPatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CovidPatientService {

    @Autowired
    private final CovidPatientRepository covidPatientRepository;

    public CovidPatient updateHospitalId(CovidPatient covidPatient){
        // get persisted record
        CovidPatient persistedCovidPatient = this.getById(covidPatient.getPatient_id());
        // update to new hospital id
        persistedCovidPatient.setHospital(covidPatient.getHospital());
        // save
        return covidPatientRepository.save(persistedCovidPatient);
    }

    public CovidPatient getById(Long Id){
        // if not found javax.persistence.EntityNotFoundException throws
        return covidPatientRepository.getById(Id);
    }

}
