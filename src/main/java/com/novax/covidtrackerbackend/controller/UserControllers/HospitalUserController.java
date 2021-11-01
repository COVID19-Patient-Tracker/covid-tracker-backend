package com.novax.covidtrackerbackend.controller.UserControllers;

import com.novax.covidtrackerbackend.model.CovidPatient;
import com.novax.covidtrackerbackend.model.Hospital;
import com.novax.covidtrackerbackend.model.HospitalVisitHistory;
import com.novax.covidtrackerbackend.model.Patient;
import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.model.*;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.HospitalService;
import com.novax.covidtrackerbackend.service.HospitalVisitHistoryService;
import com.novax.covidtrackerbackend.service.PatientServices;
import com.novax.covidtrackerbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "management/api/V1/hospital/user")
@PreAuthorize("hasAnyRole('ROLE_MOH_ADMIN,ROLE_HOSPITAL_ADMIN,ROLE_HOSPITAL_USER,ROLE_MOH_USER')")
public class HospitalUserController {

    private final HospitalService hospitalService;
    private final HospitalVisitHistoryService hospitalVisitHistoryService;
    private final UserService userService;
    private final Response response;

    @Autowired
    public HospitalUserController(HospitalService hospitalService, HospitalVisitHistoryService hospitalVisitHistoryService, UserService userService, Response response) {
        this.hospitalService = hospitalService;
        this.hospitalVisitHistoryService = hospitalVisitHistoryService;
        this.userService = userService;
        this.response = response;
    }

    @GetMapping("/getDetails/{userId}")
    public ResponseEntity<HashMap<String, Object>> getDetails(@PathVariable("userId") long userId, HttpServletRequest request) throws Exception {

        Optional<User> u = userService.getUserById(userId);

        // exclude unwanted details (pw)
        MappingJacksonValue value = new MappingJacksonValue(u.get());
        value.setSerializationView(User.WithoutPasswordViewAndHospitalInfoForHospitalUsers.class);
        User useWithOutPasswordView = (User)  value.getValue();

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("Info",useWithOutPasswordView.getHospitalUserOrAdminDetails());

        return response.getResponseEntity();
    }

    @Autowired
    private PatientServices patientServices;

    @PostMapping("/patient/add")
    @PreAuthorize("hasAnyRole('HOSPITAL_USER')")
    public ResponseEntity<HashMap<String, Object>> addPatient(@Valid @RequestBody Patient patient, HttpServletRequest request) throws IOException {
        Optional<Patient> new_patient = patientServices.addPatient(patient);
        System.out.println(patient);
        Response response = new Response();
        response.setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("patientInfo", new_patient);
        return response.getResponseEntity();


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
     * @return updatedHospitalVisitHistory
     */

    @PostMapping("/hospital/updateHistoryRecord/visitStatus")
    public ResponseEntity<HashMap<String, Object>> updateVisitStatus(@RequestBody HospitalVisitHistory hospitalVisitHistoryWithIdAndData, HttpServletRequest request) throws SQLException {
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
    public ResponseEntity<HashMap<String, Object>> updateData(@RequestBody HospitalVisitHistory hospitalVisitHistoryWithIdAndVisitStatus, HttpServletRequest request) throws SQLException {
        HospitalVisitHistory updatedHospitalVisitHistory = hospitalVisitHistoryService.updateData(hospitalVisitHistoryWithIdAndVisitStatus);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("updatedInfo",updatedHospitalVisitHistory);

        return response.getResponseEntity();
    }

    /**
     * GET ALL HOSPITALS COIVD PATIENTS
     * @param request - request object
     * @return updatedHospitalVisitHistory
     */

    @GetMapping("/hospital/covidPatients/{hospitalId}")
    public ResponseEntity<HashMap<String, Object>> getAllHospitalCovidPatients(@PathVariable("hospitalId") int hospitalId,HttpServletRequest request) throws SQLException {
        List<CovidPatient> covidPatients = hospitalService.getCovidPatientsByHospitalId(hospitalId);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("CovidPatients",covidPatients);

        return response.getResponseEntity();
    }

    /**
     * GET ALL HOSPITALS WARDS
     * @param request - request object
     * @return updatedHospitalVisitHistory
     */

    @GetMapping("/hospital/wards/{hospitalId}")
    public ResponseEntity<HashMap<String, Object>> getAllHospitalWards(@PathVariable("hospitalId") int hospitalId,HttpServletRequest request) throws SQLException {
        List<Ward> wards = hospitalService.getWardsByHospitalId(hospitalId);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("wards",wards);

        return response.getResponseEntity();
    }

    /**
     * GET ALL HOSPITALS
     * @param request - request object
     * @return updatedHospitalVisitHistory
     */

    @GetMapping("/hospitals/all")
    public ResponseEntity<HashMap<String, Object>> getAllHospitals(HttpServletRequest request) throws SQLException {
        List<Hospital> hospitals = hospitalService.getAllHospitals();

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("hospitals",hospitals);

        return response.getResponseEntity();
    }

    /**
     * UPDATES THE WARD ID OF A PATIENT VISIT HISTORY RECORD NEWEST (TRANSFER WARD)
     * @param hospitalVisitHistoryWithIdAndVisitStatus - id of the covid patient and visit status that should be updated
     * @param request - request object
     * @return updatedHospitalVisitHistory
     */

    @PostMapping("/hospital/transferWard")
    public ResponseEntity<HashMap<String, Object>> transferWard(@RequestBody HospitalVisitHistory hospitalVisitHistoryWithIdAndVisitStatus, HttpServletRequest request) throws SQLException {
        HospitalVisitHistory updatedHospitalVisitHistory = hospitalVisitHistoryService.transferWard(hospitalVisitHistoryWithIdAndVisitStatus);

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
    public ResponseEntity<HashMap<String, Object>> getNewestHospitalVisitHistoryRecord(@PathVariable("patientId") long patientId, HttpServletRequest request) throws SQLException {
        HospitalVisitHistory hospitalVisitHistories = hospitalVisitHistoryService.getNewestVisitHistoryByPatientId(patientId);

        if(hospitalVisitHistories == null){
            throw new SQLException("provided id invalid or not found in the database");
        }
        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("histories",hospitalVisitHistories);

        return response.getResponseEntity();
    }

    /**
     * GETS ALL VISIT HISTORIES OF A COVID PATIENT
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
