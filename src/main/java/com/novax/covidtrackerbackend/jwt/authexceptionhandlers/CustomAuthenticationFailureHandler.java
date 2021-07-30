package com.novax.covidtrackerbackend.jwt.authexceptionhandlers;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

        HashMap<String, String> map = new HashMap<>(4);

        map.put("uri", request.getRequestURI());
        map.put("msg", String.format("Authentication failed : [%s]", exception.getMessage()));
        map.put("exception", String.format(exception.getMessage()));
        map.put("status", String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        String resBody = objectMapper.writeValueAsString(map);
        PrintWriter printWriter = response.getWriter();

        printWriter.print(resBody);
        printWriter.flush();
        printWriter.close();

	}
}
