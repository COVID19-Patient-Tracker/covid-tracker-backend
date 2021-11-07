package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.*;
import com.novax.covidtrackerbackend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServices {

    private PatientRepository patientRepository;

    @Autowired
    public PatientServices(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients(){

        return (List<Patient>) patientRepository.findAll();
    }

    public Patient save(Patient patient){
        Patient p = patientRepository.save(patient);
        return p;
    }

    public Optional<Patient> getPatientById(Long id) throws SQLException {
        Optional<Patient> patient = patientRepository.findById(id);
        if(patient.isEmpty()){
            throw new SQLException("requested data doesn't exists in database");
        }
        return patient;
    }

    public boolean isPatientExist(String nic, String first_name, String last_name) {
        return patientRepository.isPatientExist(nic,first_name,last_name);
    }
    public List<Patient> getAllPatientByHospitalID(long id){
        return patientRepository.findPatientByhospital_id(id);
    }

    // get the newest record
    public Patient getNewestPatientDetailsBYId(long patientId) {
        return patientRepository.findNewestRecordByPatientId(patientId);
    }

    // update patient details
    public Patient updatePatientDetails(Patient patient){

        // get original record
        Patient originalPatientDetails = this.getNewestPatientDetailsBYId(patient.getPatient_id());
        // change details of original record
        originalPatientDetails.setNic(patient.getNic());
        originalPatientDetails.setHospital_id(patient.getHospital_id());
        originalPatientDetails.setAddress(patient.getAddress());
        originalPatientDetails.setGender(patient.getGender());
        originalPatientDetails.setDob(patient.getDob());
        originalPatientDetails.setAge(patient.getAge());
        originalPatientDetails.setContact_no(patient.getContact_no());
        originalPatientDetails.setFirst_name(patient.getFirst_name());
        originalPatientDetails.setLast_name(patient.getLast_name());
        originalPatientDetails.setIs_user(patient.getIs_user());
        originalPatientDetails.setIs_child(patient.getIs_child());
        // save updated original record
        return patientRepository.save(originalPatientDetails);

    }

    public Optional<Patient> getUserById(Long id) throws SQLException{
        Optional<Patient> patient = patientRepository.findById(id);
        if(patient.isEmpty()){
            throw new SQLException("requested data doesn't exists in database");
        }
        return patient;
    }

    public Optional<Patient> addPatient(Patient patient){
        Optional<Patient> created_patient = patientRepository.addPatient(
                patient.getNic(),
                patient.getHospital_id(),
                patient.getAddress(),
                patient.getFirst_name(),
                patient.getLast_name(),
                patient.getGender(),
                patient.getDob(),
                patient.getAge(),
                patient.getContact_no(),
                patient.getIs_user(),
                patient.getIs_child(),
                patient.getWard_id(),
                patient.getVisit_date(),
                patient.getData(),
                patient.getVisit_status()
        );
        return created_patient;
    }
}
