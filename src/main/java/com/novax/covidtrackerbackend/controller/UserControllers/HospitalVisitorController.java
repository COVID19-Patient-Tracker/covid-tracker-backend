package com.novax.covidtrackerbackend.controller.UserControllers;

import com.novax.covidtrackerbackend.model.dao.PcrTestDAO;
import com.novax.covidtrackerbackend.model.dao.RapidAntigenTestDAO;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.PcrTestDAOService;
import com.novax.covidtrackerbackend.service.RapidAntigenTestDAOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("management/api/V1/visitor")
public class HospitalVisitorController {

    @Autowired
    private PcrTestDAOService pcrTestDAOService;
    @Autowired
    private RapidAntigenTestDAOService rapidAntigenTestDAOService;
    @Autowired
    private Response response;

    /**
     * GET A LIST OF PCR RESULTS
     * @param patientId  - Id of the patient to get the data
     * @param request - HttpServletRequest object to access uri
     * @return - a list of pcr test records
     */

    @GetMapping("/pcr/{patientId}")
    public ResponseEntity<HashMap<String, Object>> getPcrTests(@PathVariable("patientId") Long patientId,  HttpServletRequest request){
        List<PcrTestDAO> tests = pcrTestDAOService.getPcrTestByPatientID(patientId);
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setURI(request.getRequestURI())
                .addField("TestData", tests);
        return response.getResponseEntity();
    }

    /**
     * GET A LIST OF ANTIGEN RESULTS
     * @param patientId  - Id of the patient to get the data
     * @param request - HttpServletRequest object to access uri
     * @return - a list of antigen test records
     */
    @GetMapping("/antigen/{patientId}")
    public ResponseEntity<HashMap<String, Object>> getAntigenTests(@PathVariable("patientId") Long patientId,  HttpServletRequest request){
        List<RapidAntigenTestDAO> antigen_tests = rapidAntigenTestDAOService.getAntigenTestByPatientID(patientId);
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setURI(request.getRequestURI())
                .addField("TestData", antigen_tests);
        return response.getResponseEntity();
    }
}
