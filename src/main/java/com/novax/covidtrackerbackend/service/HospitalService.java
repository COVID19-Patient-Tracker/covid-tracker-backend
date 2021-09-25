package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.Hospital;
import com.novax.covidtrackerbackend.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class HospitalService {

    private HospitalRepository hospitalRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    // TODO : do integration testing, unit testing done
    public Hospital save(Hospital hospital){
        Hospital h = hospitalRepository.save(hospital); // repo methods already tested
        return h;
    }

    // TODO : do integration testing, unit testing done
    public List<Hospital> getAllHospitals(){
        return (List<Hospital>) hospitalRepository.findAll();
    }

    // TODO : do integration testing, unit testing done
    public Optional<Hospital> getHospitalById(int Id){
        return hospitalRepository.findById(Id);
    }

    // TODO : do integration testing, unit testing done
    public void deleteHospitalById(int Id){
        hospitalRepository.deleteById(Id);
    }
}
