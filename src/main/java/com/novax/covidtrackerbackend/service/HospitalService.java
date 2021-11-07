package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.CovidPatient;
import com.novax.covidtrackerbackend.model.Hospital;
import com.novax.covidtrackerbackend.model.HospitalVisitHistory;
import com.novax.covidtrackerbackend.model.Ward;
import com.novax.covidtrackerbackend.repository.CovidPatientRepository;
import com.novax.covidtrackerbackend.repository.HospitalRepository;
import com.novax.covidtrackerbackend.repository.WardRepository;
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

    // get the newest record
    public Hospital getNewestHospitalDetialsBYHosId(int hospitalId) {
        return hospitalRepository.findNewestRecordByHospitalId(hospitalId);
    }

    // update hospital details
    public Hospital updateHospitalDetails(Hospital hospital){

        // get original record
        Hospital originalHospitalDetails
                = this.getNewestHospitalDetialsBYHosId(hospital.getHospital_id());
        // change details of original record
        originalHospitalDetails.setName(hospital.getName());
        originalHospitalDetails.setAddress(hospital.getAddress());
        originalHospitalDetails.setTelephone(hospital.getTelephone());
        originalHospitalDetails.setCapacity(hospital.getCapacity());
        // save updated original record
        return hospitalRepository.save(originalHospitalDetails);

    }

    public void deleteHospitalById(int Id){
        hospitalRepository.deleteById(Id);
    }
}
