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

    public boolean isPatientExist(String email) {
        return patientRepository.isPatientExist(email);
    }

    public Optional<Patient> getUserByNic_Fn_Ln(String email){
        Optional<Patient> patient = patientRepository.getUserByNic_Fn_Ln(email);
        return patient;
    }

    public Optional<Patient> addPatient(Patient patient){
        Optional<Patient> created_patient = patientRepository.addPatient(
                patient.getNic(),
                patient.getFirst_name(),
                patient.getLast_name(),
                patient.getEmail(),
                patient.getPassword(),
                patient.getIs_child()
        );
        return created_patient;
    }
}
