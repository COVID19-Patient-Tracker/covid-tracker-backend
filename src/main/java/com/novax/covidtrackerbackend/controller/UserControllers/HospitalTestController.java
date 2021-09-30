package com.novax.covidtrackerbackend.controller.UserControllers;

import com.novax.covidtrackerbackend.model.dao.PcrTestDAO;
import com.novax.covidtrackerbackend.model.dto.AddPcrTestRequestDTO;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.PcrTestDAOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private Response response;

    /**
     * ADD NEW PCR TEST RECORD
     * @param data  - New pcr test data added by hospital user
     * @param request - HttpServletRequest object to access uri
     * @return - created test record details
     * @throws SQLException if foreign key violated
     */

    @PostMapping("/pcr/add")
    public ResponseEntity<HashMap<String, Object>> addPcrTest(@Valid @RequestBody AddPcrTestRequestDTO data, HttpServletRequest request) throws SQLException {

        PcrTestDAO new_test = pcrTestDAOService.addPcrTest(data);
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("New pcr test added")
                .setURI(request.getRequestURI())
                .addField("TestData", new_test);
        return response.getResponseEntity();
    }
}

