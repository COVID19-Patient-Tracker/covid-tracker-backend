package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.dao.PcrTestDAO;
import com.novax.covidtrackerbackend.model.dto.AddPcrTestRequestDTO;
import com.novax.covidtrackerbackend.repository.PcrTestDAORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

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
}
