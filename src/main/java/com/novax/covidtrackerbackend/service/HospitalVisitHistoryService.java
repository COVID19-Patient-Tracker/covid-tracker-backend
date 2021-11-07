package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.CovidPatient;
import com.novax.covidtrackerbackend.model.HospitalVisitHistory;
import com.novax.covidtrackerbackend.model.Patient;
import com.novax.covidtrackerbackend.repository.CovidPatientRepository;
import com.novax.covidtrackerbackend.repository.HospitalVisitHistoryRepository;
import com.novax.covidtrackerbackend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalVisitHistoryService {

    @Autowired
    private final HospitalVisitHistoryRepository hospitalVisitHistoryRepository;
    @Autowired
    private final CovidPatientRepository covidPatientRepository;
    @Autowired
    private final PatientRepository patientRepository;

    // saves a visit history record
    public void save(HospitalVisitHistory hospitalVisitHistory) {
        hospitalVisitHistoryRepository.save(hospitalVisitHistory);
    }

    // get all visit history records
    public List<HospitalVisitHistory> getAllVisitHistoriesByPatientId(Long patientId){
        return hospitalVisitHistoryRepository.findAllHistoriesByPatientId(patientId);
    }

    // get the newest visit history record
    public HospitalVisitHistory getNewestVisitHistoryByPatientId(Long patientId) {
            return hospitalVisitHistoryRepository.findNewestRecordByPatientId(patientId);
    }

    // transfer ward
    public HospitalVisitHistory transferWard(HospitalVisitHistory hospitalVisitHistoryWithIdAndData){

        // get original record
        HospitalVisitHistory originalHospitalVisitHistory
                = this.getNewestVisitHistoryByPatientId(hospitalVisitHistoryWithIdAndData.getPatient_id());
        // change Data of original record
        originalHospitalVisitHistory.setWard_id(hospitalVisitHistoryWithIdAndData.getWard_id());
        // save updated original record
        return hospitalVisitHistoryRepository.save(originalHospitalVisitHistory);

    }

    // update visit history record data
    public HospitalVisitHistory updateData(HospitalVisitHistory hospitalVisitHistoryWithIdAndData){

        // get original record
        HospitalVisitHistory originalHospitalVisitHistory
                = this.getNewestVisitHistoryByPatientId(hospitalVisitHistoryWithIdAndData.getPatient_id());
        // change Data of original record
        originalHospitalVisitHistory.setData(hospitalVisitHistoryWithIdAndData.getData());
        // save updated original record
        return hospitalVisitHistoryRepository.save(originalHospitalVisitHistory);

    }

    // update visit status of visit history record
    public HospitalVisitHistory updateVisitStatus(HospitalVisitHistory hospitalVisitHistoryWithIdAndData){

        // get original record
        HospitalVisitHistory originalHospitalVisitHistory
                = this.getNewestVisitHistoryByPatientId(hospitalVisitHistoryWithIdAndData.getPatient_id());
        // change visit status of original record
        originalHospitalVisitHistory.setVisit_status(hospitalVisitHistoryWithIdAndData.getVisit_status());
        // save updated original record
        return hospitalVisitHistoryRepository.save(originalHospitalVisitHistory);

    }

    // transfer hospital of a covid patient
    public CovidPatient transfer(HospitalVisitHistory hospitalVisitHistory) throws SQLException {

        // change covid patient table hospital id
        CovidPatient covidPatient = covidPatientRepository.getById(hospitalVisitHistory.getPatient_id());
        // change hospital id if new hospital id is in record
        if(covidPatient.getHospital().getHospital_id() == hospitalVisitHistory.getHospital().getHospital_id()){
            throw new SQLException("Hospital doesn't changed");
        }else{
            // TODO : change patient table hospital id
            // add record to the hospital visit history table
            // get original record
            Patient originalPatientDetail
                    = patientRepository.findNewestRecordByPatientId(hospitalVisitHistory.getPatient_id());
            // change Data of original record
            originalPatientDetail.setHospital_id(hospitalVisitHistory.getHospital().getHospital_id());

            //get patient from
            patientRepository.save(originalPatientDetail);
            this.save(hospitalVisitHistory);
            // update covid patient table
            covidPatient.setHospital(hospitalVisitHistory.getHospital());
            // save new record
            return covidPatientRepository.save(covidPatient);
        }
    }
}

