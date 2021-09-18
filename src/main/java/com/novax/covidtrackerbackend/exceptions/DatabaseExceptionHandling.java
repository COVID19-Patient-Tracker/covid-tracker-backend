package com.novax.covidtrackerbackend.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;


@ControllerAdvice
public class DatabaseExceptionHandling {

    // Handle database errors
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<HashMap> databaseError(SQLException ex,HttpServletRequest request) {
        int response_code;
        String message = ex.getMessage();

        if(message.equals("user already exists in db")){
            response_code = HttpServletResponse.SC_CONFLICT;
        }else if(message.equals("invalid role")){
            response_code = HttpServletResponse.SC_BAD_REQUEST;
        }else if(message.equals("invalid hospital id or hospital doesn't exists")){
            response_code = HttpServletResponse.SC_NOT_ACCEPTABLE;
        }else{
            response_code = HttpServletResponse.SC_EXPECTATION_FAILED;
        }

        HashMap<String, String> map = new HashMap<>(4);
        map.put("uri", request.getRequestURI());
        map.put("msg", String.format(ex.getMessage()));
        map.put("exception", String.format(ex.getMessage()));
        map.put("status", String.valueOf(response_code));

        return ResponseEntity
                .status(response_code)
                .body(map);
    }

}