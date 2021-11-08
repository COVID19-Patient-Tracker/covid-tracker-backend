package com.novax.covidtrackerbackend.controller.UserControllers;

import com.novax.covidtrackerbackend.model.HospitalVisitHistory;
import com.novax.covidtrackerbackend.model.Patient;
import com.novax.covidtrackerbackend.model.dao.PcrTestDAO;
import com.novax.covidtrackerbackend.model.dao.RapidAntigenTestDAO;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.HospitalVisitHistoryService;
import com.novax.covidtrackerbackend.service.PatientServices;
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
import java.util.Optional;

@RestController
@RequestMapping("management/api/V1/visitor")
public class HospitalVisitorController {

    @Autowired
    private PcrTestDAOService pcrTestDAOService;
    @Autowired
    private RapidAntigenTestDAOService rapidAntigenTestDAOService;
    @Autowired
    private Response response;
    @Autowired
    private PatientServices patientServices;
    @Autowired
    private HospitalVisitHistoryService hospitalVisitHistoryService;

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

    /**
     * GET THE DETAILS OF THE USER
     * @param userId  - ID of the patient to get the data
     * @param request - HttpServletRequest object to access uri
     * @return - a list of antigen test records
     */
    @GetMapping("/get/{visitorId}")
    public ResponseEntity<HashMap<String, Object>> getPatientDetails(@PathVariable("visitorId") long userId, HttpServletRequest request) throws Exception {

        Optional<Patient> patient = patientServices.getPatientById(userId);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("Info",patient);

        return response.getResponseEntity();
    }

    /**
     * GETS ALL VISIT HISTORIES OF A VISITOR
     * @param patientId - id of the covid patient
     * @param request - request object
     * @return hospitalVisitHistories
     */

    @GetMapping("/getVisitHistory/{patientId}")
    public ResponseEntity<HashMap<String, Object>> getHospitalVisitHistories(@PathVariable("patientId") long patientId, HttpServletRequest request){
        List<HospitalVisitHistory> hospitalVisitHistories = hospitalVisitHistoryService.getAllVisitHistoriesByPatientId(patientId);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("histories",hospitalVisitHistories);

        return response.getResponseEntity();
    }
}
