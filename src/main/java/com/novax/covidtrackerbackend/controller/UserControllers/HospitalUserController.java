package com.novax.covidtrackerbackend.controller.UserControllers;

import com.novax.covidtrackerbackend.model.CovidPatient;
import com.novax.covidtrackerbackend.model.Hospital;
import com.novax.covidtrackerbackend.model.HospitalVisitHistory;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.HospitalService;
import com.novax.covidtrackerbackend.service.HospitalVisitHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping(path = "management/api/V1/hospital/user")
//@PreAuthorize("hasAnyRole('ROLE_MOH_USER,ROLE_HOSPITAL_USER')")
public class HospitalUserController {

    private final HospitalService hospitalService;
    private final HospitalVisitHistoryService hospitalVisitHistoryService;
    private final Response response;

    @Autowired
    public HospitalUserController(HospitalService hospitalService, HospitalVisitHistoryService hospitalVisitHistoryService, Response response) {
        this.hospitalService = hospitalService;
        this.hospitalVisitHistoryService = hospitalVisitHistoryService;
        this.response = response;
    }

    /**
     * TRANSFER HOSPITAL
     * @param hospitalVisitHistory - new record of for transferred hospital.
     * @return - If database validation passes, returns updatedInfo as JSON object.
     */

    @PostMapping("/hospital/transfer")
    public ResponseEntity<HashMap<String, Object>> transferHospital(@RequestBody HospitalVisitHistory hospitalVisitHistory, HttpServletRequest request) throws SQLException {
        // TODO : change patient table hospital id
        // add record to the hospital visit history table
        // change COVID-19 patient table hospital id
        CovidPatient UpdatedCovidPatient = hospitalVisitHistoryService.transfer(hospitalVisitHistory);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("updatedInfo",UpdatedCovidPatient);

        return response.getResponseEntity();
    }

    /**
     * GETS HOSPITAL INFO BY ID
     * @param hospitalId - Id of the hospital.
     * @return - If database validation passes, returns hospital as JSON object.
     */

    @PostMapping("/hospital/get/{hospitalId}")
    public ResponseEntity<HashMap<String, Object>> updateDataOfHistoryRecordById(@PathVariable("hospitalId") int hospitalId, HttpServletRequest request) throws Exception {
        // get hospital by id
        Optional<Hospital> hospitalObject = hospitalService.getHospitalById(hospitalId);

        if(hospitalObject.isEmpty()){
            throw new SQLException("requested id not in the database");
        }

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("hospitalInfo",hospitalObject);

        return response.getResponseEntity();
    }

    @PostMapping("/hospital/updateHistoryRecord/visitStatus")
    public ResponseEntity<HashMap<String, Object>> updateVisitStatus(@RequestBody HospitalVisitHistory hospitalVisitHistoryWithIdAndData, HttpServletRequest request) throws SQLException {
        HospitalVisitHistory updatedHospitalVisitHistory = hospitalVisitHistoryService.updateVisitStatus(hospitalVisitHistoryWithIdAndData);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("updatedInfo",updatedHospitalVisitHistory);

        return response.getResponseEntity();
    }

    @PostMapping("/hospital/updateHistoryRecord/data")
    public ResponseEntity<HashMap<String, Object>> updateData(@RequestBody HospitalVisitHistory hospitalVisitHistoryWithIdAndData, HttpServletRequest request) throws SQLException {
        HospitalVisitHistory updatedHospitalVisitHistory = hospitalVisitHistoryService.updateData(hospitalVisitHistoryWithIdAndData);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("updatedInfo",updatedHospitalVisitHistory);

        return response.getResponseEntity();
    }

    @GetMapping("/hospital/getNewestHospitalVisitRecord/{patientId}")
    public ResponseEntity<HashMap<String, Object>> getNewestHospitalVisitHistoryRecord(@PathVariable("patientId") long patientId, HttpServletRequest request) throws SQLException {
        HospitalVisitHistory hospitalVisitHistories = hospitalVisitHistoryService.getNewestVisitHistoryByPatientId(patientId);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("histories",hospitalVisitHistories);

        return response.getResponseEntity();
    }

    @GetMapping("/hospital/getHospitalVisitHistories/{patientId}")
    public ResponseEntity<HashMap<String, Object>> getHospitalVisitHistories(@PathVariable("patientId") long patientId, HttpServletRequest request) throws SQLException {
        List<HospitalVisitHistory> hospitalVisitHistories = hospitalVisitHistoryService.getAllVisitHistoriesByPatientId(patientId);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("histories",hospitalVisitHistories);

        return response.getResponseEntity();
    }
}
