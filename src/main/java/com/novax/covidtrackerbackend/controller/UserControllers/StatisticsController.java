package com.novax.covidtrackerbackend.controller.UserControllers;


import com.novax.covidtrackerbackend.model.Statistics;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.HospitalService;
import com.novax.covidtrackerbackend.service.StatisticsService;
import com.novax.covidtrackerbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * this controller provides resources for the MOH admin
 */
@RestController
@RequestMapping(path = "management/api/V1/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final Response response;

    @Autowired
    public StatisticsController(StatisticsService statisticsService, Response response) {
        this.statisticsService = statisticsService;
        this.response = response;
    }

    // dashboard related data returned by this method
    @GetMapping("/all")
    public ResponseEntity<HashMap<String, Object>> getAllStatistics(HttpServletRequest request) {
        List<Statistics> statistics = statisticsService.getAllStatisticsByDateASC();
        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("statistics",statistics);
        return response.getResponseEntity();
    }
}
