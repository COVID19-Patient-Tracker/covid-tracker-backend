package com.novax.covidtrackerbackend.exceptions;
import com.novax.covidtrackerbackend.response.Response;
import org.springframework.dao.EmptyResultDataAccessException;
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

    /**
     * HANDLES EMPTY DATA ACCESS ERROR IN DATABASE
     * @param ex - exception thrown
     * @param request - request mad by the client
     * @return ResponseEntity
     */
    // Handle empty result data access exceptions
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<HashMap<String,Object>> databaseError(EmptyResultDataAccessException ex,HttpServletRequest request) {
        int response_code = HttpServletResponse.SC_NOT_FOUND;
        //String message = ex.getMessage();

        Response response = new Response()
                .setMessage("requested data doesn't exist in the database")
                .setResponseCode(response_code)
                .setURI(request.getRequestURI())
                .setException(ex);
//        HashMap<String, String> map = new HashMap<>(4);
//        map.put("msg", "requested data doesn't exist in the database");
//        map.put("uri", request.getRequestURI());
//        map.put("exception", String.format(ex.getMessage()));
//        map.put("status", String.valueOf(response_code));

        return response.getResponseEntity();
//        return ResponseEntity
//                .status(response_code)
//                .body(map);
    }

    /**
     * Handle database errors
     * @param ex - exception thrown
     * @param request - request mad by the client
     * @return ResponseEntity
     */


    @ExceptionHandler(SQLException.class)
    public ResponseEntity<HashMap<String,Object>> databaseError(SQLException ex,HttpServletRequest request) {
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

        Response response = new Response()
                .setMessage("Database exception occurred")
                .setResponseCode(response_code)
                .setURI(request.getRequestURI())
                .setException(ex);
        return response.getResponseEntity();

//        HashMap<String, String> map = new HashMap<>(4);
//        map.put("uri", request.getRequestURI());
//        map.put("msg", String.format(ex.getMessage()));
//        map.put("exception", String.format(ex.getMessage()));
//        map.put("status", String.valueOf(response_code));
//
//        return ResponseEntity
//                .status(response_code)
//                .body(map);
    }

}