package com.novax.covidtrackerbackend.controller.UserControllers;

import com.novax.covidtrackerbackend.model.Patient;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.PatientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("management/api/V1/hospital/user")

public class HospitalUserController {

    @Autowired
    private PatientServices patientServices;

    @PostMapping("/patient/add")

    public ResponseEntity<HashMap<String, Object>> addPatient(@Valid @RequestBody Patient patient, HttpServletRequest request) throws IOException {
        Optional<Patient> new_patient = patientServices.addPatient(patient);
        System.out.println(patient);
        Response<Object> response = new Response<>();
        response.setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI());
        return response.getResponseEntity();


    }
}