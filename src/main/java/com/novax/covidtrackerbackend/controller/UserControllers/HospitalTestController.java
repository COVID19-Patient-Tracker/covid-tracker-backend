package com.novax.covidtrackerbackend.controller.UserControllers;

import com.novax.covidtrackerbackend.model.dao.PcrTestDAO;
import com.novax.covidtrackerbackend.model.dao.RapidAntigenTestDAO;
import com.novax.covidtrackerbackend.model.dto.AddTestRequestDTO;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.PcrTestDAOService;
import com.novax.covidtrackerbackend.service.RapidAntigenTestDAOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.HashMap;

@RestController
@RequestMapping("management/api/V1/hospital/user")
public class HospitalTestController {

    @Autowired
    private PcrTestDAOService pcrTestDAOService;
    @Autowired
    private RapidAntigenTestDAOService rapidAntigenTestDAOService;
    @Autowired
    private Response response;

    /**
     * ADD NEW PCR TEST RECORD
     * @param data  - New pcr test data added by hospital user
     * @param request - HttpServletRequest object to access uri
     * @return - created test record details
     * @throws SQLException if foreign key violated
     */

    @PostMapping("/pcr/add")
    public ResponseEntity<HashMap<String, Object>> addPcrTest(@Valid @RequestBody AddTestRequestDTO data, HttpServletRequest request) throws SQLException {

        PcrTestDAO new_test = pcrTestDAOService.addPcrTest(data);
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("New pcr test added")
                .setURI(request.getRequestURI())
                .addField("TestData", new_test);
        return response.getResponseEntity();
    }

    /**
     * UPDATE PCR TEST STATE
     * @param data  - Updated values send my hospital user
     * @param testId - Id of the test which needs to be updated
     * @param request - HttpServletRequest object to access uri
     * @return - updated test record details
     * @throws SQLException if foreign key violated
     */

    @PutMapping("/pcr/edit/{testId}")
    public ResponseEntity<HashMap<String, Object>> updatePcrTestState(@Valid @RequestBody AddTestRequestDTO data,
                                                                      @PathVariable("testId") Integer testId,
                                                                      HttpServletRequest request) throws SQLException {

        try {
            PcrTestDAO updated_test = pcrTestDAOService.updatePcrTestStatus(testId , data.getTest_result());
            response.reset().setResponseCode(HttpStatus.OK.value())
                    .setMessage("State updated successfully")
                    .setURI(request.getRequestURI())
                    .addField("TestData", updated_test);
            return response.getResponseEntity();
        } catch(EntityNotFoundException ex){
            response.reset().setResponseCode(HttpStatus.NOT_FOUND.value())
                    .setException(ex)
                    .setMessage("Invalid Test ID Provided")
                    .setURI(request.getRequestURI());
            return response.getResponseEntity();
        }
    }

    /**
     * ADD NEW ANTIGEN TEST RECORD
     * @param test_data  - New antigen test data added by hospital user
     * @param request - HttpServletRequest object to access uri
     * @return - created test record details
     * @throws SQLException if foreign key violated
     */

    @PostMapping("/antigen/add")
    public ResponseEntity<HashMap<String, Object>> addAntigenTest(@Valid @RequestBody AddTestRequestDTO test_data, HttpServletRequest request) throws SQLException {

        RapidAntigenTestDAO antigen_test = rapidAntigenTestDAOService.addAntigenTest(test_data);
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("New antigen test record added")
                .setURI(request.getRequestURI())
                .addField("TestData", antigen_test);
        return response.getResponseEntity();
    }

    /**
     * UPDATE ANTIGEN TEST STATE
     * @param test_data  - Updated values send my hospital user
     * @param testId - Id of the test which needs to be updated
     * @param request - HttpServletRequest object to access uri
     * @return - updated test record details
     * @throws SQLException if foreign key violated
     */

    @PutMapping("/antigen/edit/{testId}")
    public ResponseEntity<HashMap<String, Object>> updateAntigenTestState(@Valid @RequestBody AddTestRequestDTO test_data,
                                                                      @PathVariable("testId") Integer testId,
                                                                      HttpServletRequest request) throws SQLException {

        try {
            RapidAntigenTestDAO updated_data = rapidAntigenTestDAOService.updateAntigenTestStatus(testId , test_data.getTest_result());
            response.reset().setResponseCode(HttpStatus.OK.value())
                    .setMessage("Test Status updated successfully")
                    .setURI(request.getRequestURI())
                    .addField("TestData", updated_data);
            return response.getResponseEntity();
        } catch(EntityNotFoundException ex){
            response.reset().setResponseCode(HttpStatus.NOT_FOUND.value())
                    .setException(ex)
                    .setMessage("Invalid Test ID Provided")
                    .setURI(request.getRequestURI());
            return response.getResponseEntity();
        }
    }
}

