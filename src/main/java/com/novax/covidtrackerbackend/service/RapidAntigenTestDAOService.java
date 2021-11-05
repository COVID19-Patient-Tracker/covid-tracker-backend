package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.dao.PcrTestDAO;
import com.novax.covidtrackerbackend.model.dao.RapidAntigenTestDAO;
import com.novax.covidtrackerbackend.model.dto.AddTestRequestDTO;
import com.novax.covidtrackerbackend.repository.RapidAntigenTestDAORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RapidAntigenTestDAOService {

    @Autowired
    private final RapidAntigenTestDAORepository rapidAntigenTestDAORepository;

    /**
     * ADD NEW RAPIDANTIGEN TEST RECORD TO `rapidantigentest` TABLE
     * @param testData
     * @return created Antigen Test Record data
     */

    public RapidAntigenTestDAO addAntigenTest(AddTestRequestDTO testData)  throws SQLException {
        RapidAntigenTestDAO antigenTest = new RapidAntigenTestDAO(
                testData.getPatientId(),
                testData.getHospital_id(),
                testData.getTest_data(),
                testData.getTest_result());
        rapidAntigenTestDAORepository.save(antigenTest);
        return antigenTest;
    }

    /**
     * UPDATE ANTIGEN TEST STATE
     * @param test_id Unique ID of the test which needs to be updated
     * @param test_state New value for the test status
     * @return updates antigen test details
     */

    public RapidAntigenTestDAO updateAntigenTestStatus( Integer test_id,  String test_state)  throws SQLException {
        Optional<RapidAntigenTestDAO> testFound = rapidAntigenTestDAORepository.findById(test_id);
        if (testFound.isEmpty()){
            throw new EntityNotFoundException("Invalid Test ID Provided");
        }
        RapidAntigenTestDAO testData = testFound.get();
        if (test_state != null && !testData.getTest_result().equals(test_state)){
            testData.setTest_result(test_state);
        }
        rapidAntigenTestDAORepository.save(testData);
        return testData;
    }

    /**
     * GET THE LIST OF RAPID ANTIGEN TESTS FOR A PATIENT
     * @param patient_id Unique ID of the test which needs to be updated
     * @return A list of Antigen test data
     */
    public List<RapidAntigenTestDAO> getAntigenTestByPatientID(Long patient_id){
        return rapidAntigenTestDAORepository.findByPatientId(patient_id);
    }
}