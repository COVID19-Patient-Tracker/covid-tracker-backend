package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.CovidPatient;
import com.novax.covidtrackerbackend.model.Hospital;
import com.novax.covidtrackerbackend.model.HospitalVisitHistory;
import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.model.dao.PcrTestDAO;
import com.novax.covidtrackerbackend.repository.CovidPatientRepository;
import com.novax.covidtrackerbackend.repository.HospitalVisitHistoryRepository;
import com.novax.covidtrackerbackend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HospitalVisitHistoryServiceTest {

    @Mock private HospitalVisitHistoryRepository hospitalVisitHistoryRepository;
    @Mock private CovidPatientRepository covidPatientRepository;
    private HospitalVisitHistoryService underTest;

    @BeforeEach
    void setUp() {
        underTest = new HospitalVisitHistoryService(hospitalVisitHistoryRepository,covidPatientRepository);
    }

    // data for saving
    HospitalVisitHistory hospitalVisitHistory = new HospitalVisitHistory();


    @Test
    void isAbleToSave() {
        // when
        underTest.save(hospitalVisitHistory);
        // then
        verify(hospitalVisitHistoryRepository).save(hospitalVisitHistory);
    }

    @Test
    void isAbleToGetAllVisitHistoriesByPatientId() {
        // when
        List<HospitalVisitHistory> hospitalVisitHistories =  underTest.getAllVisitHistoriesByPatientId(anyLong());

        // then
        verify(hospitalVisitHistoryRepository).findAllHistoriesByPatientId(anyLong());
    }

    @Test
    void updateData() {
        // data for saving
        HospitalVisitHistory UpdateDataOfhospitalVisitHistory = new HospitalVisitHistory(
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
        underTest.updateData(UpdateDataOfhospitalVisitHistory);

        // then
        verify(hospitalVisitHistoryRepository).findNewestRecordByPatientId(anyLong());
        verify(hospitalVisitHistoryRepository).save(hospitalVisitHistory);
    }

    @Test
    void updateVisitStatus() {
        // data for saving
        HospitalVisitHistory UpdateDataOfhospitalVisitHistory = new HospitalVisitHistory(
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
        underTest.updateData(UpdateDataOfhospitalVisitHistory);

        // then
        verify(hospitalVisitHistoryRepository).findNewestRecordByPatientId(anyLong());
        verify(hospitalVisitHistoryRepository).save(hospitalVisitHistory);
    }

    @Test
    void itShouldtransfer() throws SQLException {
        HospitalVisitHistory UpdateDataOfhospitalVisitHistory = new HospitalVisitHistory(
                99L,
                99L,
                new Hospital(99,null,null,null,800,null,null),
                null,
                99,
                "Updated data",
                null
        );

        // given

        given(covidPatientRepository.getById(99L))
                .willReturn(new CovidPatient(
                        99L,
                        new Hospital(98,null,null,null,800,null,null),
                        new Date(),
                        "null"
                ));

        // when
        underTest.transfer(UpdateDataOfhospitalVisitHistory);

        // then
        verify(covidPatientRepository).getById(anyLong());
        verify(hospitalVisitHistoryRepository).save(UpdateDataOfhospitalVisitHistory);
    }

    @Test
    void getNewestVisitHistoryByPatientId() {
        // when
        HospitalVisitHistory hospitalVisitHistory =  underTest.getNewestVisitHistoryByPatientId(anyLong());

        // then
        verify(hospitalVisitHistoryRepository).findNewestRecordByPatientId(anyLong());
    }
}