package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.CovidPatient;
import com.novax.covidtrackerbackend.model.Hospital;
import com.novax.covidtrackerbackend.model.HospitalVisitHistory;
import com.novax.covidtrackerbackend.repository.CovidPatientRepository;
import com.novax.covidtrackerbackend.repository.HospitalVisitHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospitalVisitHistoryService {
    @Autowired
    private HospitalVisitHistoryRepository hospitalVisitHistoryRepository;
    @Autowired
    private CovidPatientRepository covidPatientRepository;


    public void save(HospitalVisitHistory hospitalVisitHistory) {
        hospitalVisitHistoryRepository.save(hospitalVisitHistory);
    }

    public List<HospitalVisitHistory> getAllVisitHistoriesByPatientId(Long patientId){
        return hospitalVisitHistoryRepository.findAllHistoriesByPatientId(patientId);
    }

    public HospitalVisitHistory getNewestVisitHistoryByPatientId(Long patientId){
        return hospitalVisitHistoryRepository.findNewestRecordByPatientId(patientId);
    }

    public HospitalVisitHistory updateVisitStatus(HospitalVisitHistory hospitalVisitHistoryWithIdAndData){
        HospitalVisitHistory originalHospitalVisitHistory
                = this.getNewestVisitHistoryByPatientId(hospitalVisitHistoryWithIdAndData.getPatient_id());
        originalHospitalVisitHistory.setVisit_status(hospitalVisitHistoryWithIdAndData.getVisit_status());
        return hospitalVisitHistoryRepository.save(originalHospitalVisitHistory);
    }

    public HospitalVisitHistory updateData(HospitalVisitHistory hospitalVisitHistoryWithIdAndData){
        HospitalVisitHistory originalHospitalVisitHistory
                = this.getNewestVisitHistoryByPatientId(hospitalVisitHistoryWithIdAndData.getPatient_id());
        originalHospitalVisitHistory.setData(hospitalVisitHistoryWithIdAndData.getData());
        return hospitalVisitHistoryRepository.save(originalHospitalVisitHistory);
    }

    public CovidPatient transfer(HospitalVisitHistory hospitalVisitHistory) throws SQLException {
        // change covid patient table hospital id
        CovidPatient covidPatient = covidPatientRepository.getById(hospitalVisitHistory.getPatient_id());
        // change hospital id if new hospital id is in record
        if(covidPatient.getHospital().getHospital_id() == hospitalVisitHistory.getHospital().getHospital_id()){
            throw new SQLException("Hospital doesn't changed");
        }else{
            // TODO : change patient table hospital id
            // add record to the hospital visit history table
            this.save(hospitalVisitHistory);
            covidPatient.setHospital(hospitalVisitHistory.getHospital());
            // save new record
            return covidPatientRepository.save(covidPatient);
        }
    }
}
