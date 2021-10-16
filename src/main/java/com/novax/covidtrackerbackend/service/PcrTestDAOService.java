package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.dao.PcrTestDAO;
import com.novax.covidtrackerbackend.model.dto.AddPcrTestRequestDTO;
import com.novax.covidtrackerbackend.repository.PcrTestDAORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PcrTestDAOService {

    @Autowired
    private final PcrTestDAORepository pcrTestDAORepository;

    /**
     * ADD NEW PCR TEST RECORD TO PCRTEST TABLE
     * @param testData
     * @return created PCR Record data
     */

    public PcrTestDAO addPcrTest( AddPcrTestRequestDTO testData)  throws SQLException {
        System.out.println("In the service");
        System.out.println(testData.getPatient_id());
        PcrTestDAO pcr = new PcrTestDAO(
                testData.getPatient_id(),
                testData.getHospital_id(),
                testData.getTest_data(),
                testData.getTest_result());
        pcrTestDAORepository.save(pcr);
        return pcr;
    }

    /**
     * UPDATE PCR TEST STATE
     * @param test_id Unique ID of the test which needs to be updated
     * @param test_state New value for the test status
     * @return updates pcr test details
     */

    public PcrTestDAO updatePcrTestStatus( Integer test_id,  String test_state)  throws SQLException {
        Optional<PcrTestDAO> testFound = pcrTestDAORepository.findById(test_id);
        if (testFound.isEmpty()){
            throw new EntityNotFoundException("Invalid Test ID Provided");
        }
        PcrTestDAO testData = testFound.get();
        if (test_state != null && !testData.getTest_result().equals(test_state)){
            testData.setTest_result(test_state);
        }
        pcrTestDAORepository.save(testData);
        return testData;
    }
}
