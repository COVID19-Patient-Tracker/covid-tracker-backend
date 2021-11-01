package com.novax.covidtrackerbackend.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novax.covidtrackerbackend.auth.ApplicationUser;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@NoArgsConstructor
public class CustomAuthenticationSuccessResponse {

    HashMap<String, Object> map = new HashMap<>(4);

    protected String getResponse(HttpServletResponse response, ApplicationUser userDetails, HttpServletRequest request) throws JsonProcessingException {
        // custom response for success Authentication
        map.put("uri", request.getRequestURI());
        map.put("message", String.format("Authentication success : [%s]",userDetails.getUsername()));
        map.put("email", userDetails.getUsername());
        map.put("id", userDetails.getId());
        map.put("role", userDetails.getRole());
        //map.put("authorities", userDetails.getAuthorities());
        map.put("status", String.valueOf(HttpServletResponse.SC_OK));

        response.setStatus(HttpServletResponse.SC_OK); // 200 OK
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        String resBody = objectMapper.writeValueAsString(map);

        return resBody;
    }


}
