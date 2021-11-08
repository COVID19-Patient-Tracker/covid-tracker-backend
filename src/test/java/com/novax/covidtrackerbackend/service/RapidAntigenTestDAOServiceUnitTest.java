package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.dao.PcrTestDAO;
import com.novax.covidtrackerbackend.model.dao.RapidAntigenTestDAO;
import com.novax.covidtrackerbackend.model.dto.AddTestRequestDTO;
import com.novax.covidtrackerbackend.repository.CovidPatientRepository;
import com.novax.covidtrackerbackend.repository.HospitalRepository;
import com.novax.covidtrackerbackend.repository.RapidAntigenTestDAORepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RapidAntigenTestDAOServiceUnitTest {

    @Mock
    RapidAntigenTestDAORepository rapidAntigenTestDAORepository;
    @Mock
    private CovidPatientRepository covidPatientRepository;
    @Mock
    private HospitalRepository hospitalRepository;

    RapidAntigenTestDAOService rapidAntigenTestDAOService;

    @BeforeEach
    void setUp() {
        rapidAntigenTestDAOService = new RapidAntigenTestDAOService(covidPatientRepository,hospitalRepository,rapidAntigenTestDAORepository);
    }

    //mock data
    Integer testId = 10;
    Long patientId = Long.valueOf(15600);
    int hospitalId = 1;
    Date testDate = new Date();
    String testResult = "PENDING";

    AddTestRequestDTO testData = new AddTestRequestDTO();
    RapidAntigenTestDAO antigen_test = new RapidAntigenTestDAO(patientId , hospitalId, testDate, testResult);

    String notFoundException = "Invalid Test ID Provided";

    @Test
    @DisplayName("Add Antigen Test Success")
    void addAntigenTest() throws SQLException {
        Mockito.when(rapidAntigenTestDAORepository.save(any(RapidAntigenTestDAO.class))).thenReturn(antigen_test);

        RapidAntigenTestDAO result = rapidAntigenTestDAOService.addAntigenTest(testData);
        assertThat(result).isInstanceOf(RapidAntigenTestDAO.class);
    }

    @Test
    @DisplayName("Update Antigen Test Status Success")
    void updateAntigenTestStatus() throws SQLException {
        Mockito.when(rapidAntigenTestDAORepository.findById(testId)).thenReturn(Optional.of(antigen_test));
        RapidAntigenTestDAO result = rapidAntigenTestDAOService.updateAntigenTestStatus(testId, testResult);

        assertThat(result).isInstanceOf(RapidAntigenTestDAO.class);
    }

    @Test
    @DisplayName("Update Antigen Test Record Returns Invalid Test ID Error")
    void updateAntigenTestStatusReturnNotFound() {
        Mockito.when(rapidAntigenTestDAORepository.findById(testId)).thenReturn(Optional.empty());

        Exception thrown = assertThrows(EntityNotFoundException.class, () -> {
            rapidAntigenTestDAOService.updateAntigenTestStatus(testId,testResult);
        });
        assertEquals(thrown.getMessage(), notFoundException);
    }
}