package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.CovidPatient;
import com.novax.covidtrackerbackend.model.Hospital;
import com.novax.covidtrackerbackend.model.HospitalVisitHistory;
import com.novax.covidtrackerbackend.repository.CovidPatientRepository;
import com.novax.covidtrackerbackend.repository.HospitalVisitHistoryRepository;
import com.novax.covidtrackerbackend.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HospitalVisitHistoryServiceTest {

    @Mock private HospitalVisitHistoryRepository hospitalVisitHistoryRepository;
    @Mock private CovidPatientRepository covidPatientRepository;
    @Mock private PatientRepository patientRepository;
    private HospitalVisitHistoryService underTest;

    @BeforeEach
    void setUp() {
        underTest = new HospitalVisitHistoryService(hospitalVisitHistoryRepository,covidPatientRepository,patientRepository);
    }

    // data for saving
    HospitalVisitHistory hospitalVisitHistory = new HospitalVisitHistory();


    @Test
    @DisplayName("save visit history record")
    void isAbleToSave() {
        // when
        underTest.save(hospitalVisitHistory);
        // then
        verify(hospitalVisitHistoryRepository).save(hospitalVisitHistory);
    }

    @Test
    @DisplayName("Get all visit history records as requested")
    void isAbleToGetAllVisitHistoriesByPatientId() {
        // when
        List<HospitalVisitHistory> hospitalVisitHistories =  underTest.getAllVisitHistoriesByPatientId(anyLong());

        // then
        verify(hospitalVisitHistoryRepository).findAllHistoriesByPatientId(anyLong());
    }

    @Test
    @DisplayName("Update data of visit history record")
    void updateData() throws SQLException {
        // data for saving
        HospitalVisitHistory UpdateDataOfHospitalVisitHistory = new HospitalVisitHistory(
                99L,
                99L,
                null,
                null,
                99,
                "Updated data",
                null
        );

        // given
        given(hospitalVisitHistoryRepository.findNewestRecordByPatientId(99L))
                .willReturn(hospitalVisitHistory);

        // when
        underTest.updateData(UpdateDataOfHospitalVisitHistory);

        // then
        verify(hospitalVisitHistoryRepository).findNewestRecordByPatientId(anyLong());
        verify(hospitalVisitHistoryRepository).save(hospitalVisitHistory);
    }

    @Test
    @DisplayName("Update visit status of visit history record")
    void updateVisitStatus() {
        // data for saving
        HospitalVisitHistory UpdateDataOfHospitalVisitHistory = new HospitalVisitHistory(
                99L,
                99L,
                null,
                null,
                99,
                "Updated data",
                "updated visit status"
        );

        // given
        given(hospitalVisitHistoryRepository.findNewestRecordByPatientId(99L))
                .willReturn(hospitalVisitHistory);

        // when
        underTest.updateData(UpdateDataOfHospitalVisitHistory);

        // then
        verify(hospitalVisitHistoryRepository).findNewestRecordByPatientId(anyLong());
        verify(hospitalVisitHistoryRepository).save(hospitalVisitHistory);
    }

    @Test
    @DisplayName("It should transfer hospital")
    void itShouldTransfer() throws SQLException {
        HospitalVisitHistory UpdateDataOfHospitalVisitHistory = new HospitalVisitHistory(
                99L,
                99L,
                new Hospital(99,null,null,null,800),
                null,
                99,
                "Updated data",
                null
        );

        // given

//        given(covidPatientRepository.getById(99L))
//                .willReturn(new CovidPatient(
//                        99L,
//                        new Hospital(98,null,null,null,800),
//                        new Date(),
//                        "null"
//                ));

        // when
//        underTest.transfer(UpdateDataOfHospitalVisitHistory);

        // then
//        verify(covidPatientRepository).getById(anyLong());
//        verify(hospitalVisitHistoryRepository).save(UpdateDataOfHospitalVisitHistory);
    }

    @Test
    @DisplayName("Get newest Visit history record")
    void getNewestVisitHistoryByPatientId() {
        // when
        HospitalVisitHistory hospitalVisitHistory =  underTest.getNewestVisitHistoryByPatientId(anyLong());

        // then
        verify(hospitalVisitHistoryRepository).findNewestRecordByPatientId(anyLong());
    }
}