package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.CovidPatient;
import com.novax.covidtrackerbackend.model.Hospital;
import com.novax.covidtrackerbackend.model.dao.PcrTestDAO;
import com.novax.covidtrackerbackend.model.dto.AddTestRequestDTO;
import com.novax.covidtrackerbackend.repository.CovidPatientRepository;
import com.novax.covidtrackerbackend.repository.HospitalRepository;
import com.novax.covidtrackerbackend.repository.PcrTestDAORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class PcrTestDAOService {
    @Autowired
    private final CovidPatientRepository covidPatientRepository;
    @Autowired
    private final HospitalRepository hospitalRepository;
    @Autowired
    private final PcrTestDAORepository pcrTestDAORepository;

    /**
     * ADD NEW PCR TEST RECORD TO PCRTEST TABLE
     * @param testData
     * @return created PCR Record data
     */

    public PcrTestDAO addPcrTest( AddTestRequestDTO testData)  throws SQLException {
        PcrTestDAO pcr = new PcrTestDAO(
                testData.getPatient_id(),
                testData.getHospital_id(),
                testData.getTest_data(),
                testData.getTest_result());


        // when updating PCR results if it is positive create new covid patient in table
        if(pcr.getTest_result().equals("POSITIVE")){
            Hospital hospital = hospitalRepository.findById(pcr.getHospital_id()).get();
            CovidPatient covidPatient = new CovidPatient(
                    pcr.getPatientId(),
                    hospital,
                    new Date(),
                    "ACTIVE"
            );
            covidPatientRepository.save(covidPatient);
        }
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

            // when updating PCR results if it is positive create new covid patient in table
            if(testData.getTest_result().equals("POSITIVE")){

                Hospital hospital = hospitalRepository.findById(testData.getHospital_id()).get();

                CovidPatient covidPatient = new CovidPatient(
                        testData.getPatientId(),
                        hospital,
                        new Date(),
                        "ACTIVE"
                );

                covidPatientRepository.save(covidPatient);
            }
        }
        pcrTestDAORepository.save(testData);
        return testData;
    }

    /**
     * GET THE LIST OF PCR TESTS FOR A PATIENT
     * @param patient_id Unique ID of the test which needs to be updated
     * @return A list of Pcr test data
     */
    public List<PcrTestDAO> getPcrTestByPatientID(Long patient_id){
        return pcrTestDAORepository.findByPatientId(patient_id);
    }
}
