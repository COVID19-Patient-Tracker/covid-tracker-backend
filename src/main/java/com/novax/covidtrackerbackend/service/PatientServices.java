package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.Patient;
import com.novax.covidtrackerbackend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public boolean isPatientExist(String nic, String first_name, String last_name) {
        return patientRepository.isPatientExist(nic,first_name,last_name);
    }

    public Optional<Patient> getUserByNic_Fn_Ln(String nic, String first_name, String last_name){
        Optional<Patient> patient = patientRepository.getUserByNic_Fn_Ln(nic,first_name,last_name);
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
                patient.getIs_child()
        );
        return created_patient;
    }
}
