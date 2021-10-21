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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "management/api/V1/hospital/user")
@PreAuthorize("hasAnyRole('ROLE_MOH_USER,ROLE_HOSPITAL_USER')")
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

    /**
     * UPDATES DATA OF A VISIT RECORD
     * @param hospitalVisitHistoryWithIdAndData - id of the covid patient and data that should be updated
     * @param request - request object
     * @return
     * @throws SQLException
     */
    @PostMapping("/hospital/updateHistoryRecord/visitStatus")
    public ResponseEntity<HashMap<String, Object>> updateVisitStatus(@RequestBody HospitalVisitHistory hospitalVisitHistoryWithIdAndData, HttpServletRequest request){
        // update record with new data
        HospitalVisitHistory updatedHospitalVisitHistory = hospitalVisitHistoryService.updateVisitStatus(hospitalVisitHistoryWithIdAndData);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("updatedInfo",updatedHospitalVisitHistory);

        return response.getResponseEntity();
    }

    /**
     * UPDATES THE VISIT STATUS OF A VISIT HISTORY RECORD
     * @param hospitalVisitHistoryWithIdAndVisitStatus - id of the covid patient and visit status that should be updated
     * @param request - request object
     * @return updatedHospitalVisitHistory
     */
    @PostMapping("/hospital/updateHistoryRecord/data")
    public ResponseEntity<HashMap<String, Object>> updateData(@RequestBody HospitalVisitHistory hospitalVisitHistoryWithIdAndVisitStatus, HttpServletRequest request){
        HospitalVisitHistory updatedHospitalVisitHistory = hospitalVisitHistoryService.updateData(hospitalVisitHistoryWithIdAndVisitStatus);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("updatedInfo",updatedHospitalVisitHistory);

        return response.getResponseEntity();
    }

    /**
     * GET THE NEWEST VISIT HISTORY RECORD OF A COVID PATIENT
     * @param patientId - id of the covid patient
     * @param request - request object
     * @return hospitalVisitHistories
     */
    @GetMapping("/hospital/getNewestHospitalVisitRecord/{patientId}")
    public ResponseEntity<HashMap<String, Object>> getNewestHospitalVisitHistoryRecord(@PathVariable("patientId") long patientId, HttpServletRequest request){
        HospitalVisitHistory hospitalVisitHistories = hospitalVisitHistoryService.getNewestVisitHistoryByPatientId(patientId);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("histories",hospitalVisitHistories);

        return response.getResponseEntity();
    }

    /**
     *  GETS ALL VISIT HISTORIES OF A COVID PATIENT
     * @param patientId - id of the covid patient
     * @param request - request object
     * @return hospitalVisitHistories
     */
    @GetMapping("/hospital/getHospitalVisitHistories/{patientId}")
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
