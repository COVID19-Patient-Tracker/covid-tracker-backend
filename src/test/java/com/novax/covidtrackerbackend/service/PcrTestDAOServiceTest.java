package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.dao.PcrTestDAO;
import com.novax.covidtrackerbackend.model.dto.AddPcrTestRequestDTO;
import com.novax.covidtrackerbackend.repository.PcrTestDAORepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PcrTestDAOServiceTest {

    @Mock
    PcrTestDAORepository pcrTestDAORepository;

    PcrTestDAOService pcrTestDAOService;

    @BeforeEach
    void setUp() {
        pcrTestDAOService = new PcrTestDAOService(pcrTestDAORepository);
    }

    //mock data
    Integer testId = 120;
    Long patientId = Long.valueOf(12000);
    int hospitalId = 10;
    Date testDate = new Date();
    String testResult = "PENDING";

    AddPcrTestRequestDTO  testData = new AddPcrTestRequestDTO();
    PcrTestDAO pcrTest = new PcrTestDAO(patientId , hospitalId, testDate, testResult);

    String notFoundException = "Invalid Test ID Provided";


    @Test
    @DisplayName("Add pcr Test Record Successful")
    void addPcrTest() throws SQLException {
        Mockito.when(pcrTestDAORepository.save(any(PcrTestDAO.class))).thenReturn(pcrTest);

        PcrTestDAO result = pcrTestDAOService.addPcrTest(testData);
        assertThat(result).isInstanceOf(PcrTestDAO.class);
    }

    @Test
    @DisplayName("Update pcr Test Record Success")
    void updatePcrTestStatus() throws SQLException {
        Mockito.when(pcrTestDAORepository.findById(testId)).thenReturn(Optional.of(pcrTest));
        PcrTestDAO result = pcrTestDAOService.updatePcrTestStatus(testId, testResult);

        assertThat(result).isInstanceOf(PcrTestDAO.class);
    }

    @Test
    @DisplayName("Update pcr Test Record Returns test ID invalid error")
    void updatePcrTestStatusReturnNotFound() {
        Mockito.when(pcrTestDAORepository.findById(testId)).thenReturn(Optional.empty());

        Exception thrown = assertThrows(EntityNotFoundException.class, () -> {
            pcrTestDAOService.updatePcrTestStatus(testId,testResult);
        });
        assertEquals(thrown.getMessage(), notFoundException);
    }
}