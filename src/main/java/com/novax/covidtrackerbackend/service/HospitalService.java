package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.CovidPatient;
import com.novax.covidtrackerbackend.model.Hospital;
import com.novax.covidtrackerbackend.model.Ward;
import com.novax.covidtrackerbackend.model.dto.CovidPatientDTO;
import com.novax.covidtrackerbackend.repository.CovidPatientRepository;
import com.novax.covidtrackerbackend.repository.HospitalRepository;
import com.novax.covidtrackerbackend.repository.WardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class HospitalService {

    private HospitalRepository hospitalRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private CovidPatientRepository covidPatientRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public Hospital save(Hospital hospital){
        Hospital h = hospitalRepository.save(hospital); // repo methods already tested
        return h;
    }

    public List<Hospital> getAllHospitals(){
        return (List<Hospital>) hospitalRepository.findAll();
    }

    public Optional<Hospital> getHospitalById(int Id){
        return hospitalRepository.findById(Id);
    }

    public List<CovidPatient> getCovidPatientsByHospitalId(int Id){
        Optional<Hospital> hospital = this.getHospitalById(Id);
        return covidPatientRepository.findCovidPatientsByHospital(hospital.get());
    }

    public List<Ward> getWardsByHospitalId(int Id){
        return wardRepository.findByHospitalId(Id);
    }

    public void deleteHospitalById(int Id){
        hospitalRepository.deleteById(Id);
    }
}
