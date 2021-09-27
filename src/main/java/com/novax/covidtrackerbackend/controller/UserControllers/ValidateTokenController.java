package com.novax.covidtrackerbackend.controller.UserControllers;

import com.novax.covidtrackerbackend.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;

@RestController
@RequestMapping(path = "validate/token")
@PreAuthorize
        ("hasAnyRole('" +
            "ROLE_MOH_ADMIN," +
            "ROLE_HOSPITAL_ADMIN," +
            "ROLE_HOSPITAL_USER," +
            "ROLE_MOH_USER," +
            "ROLE_MOH_ADMIN')" +
        "")
public class ValidateTokenController {
    @Autowired
    Response response;

    @PostMapping("/jwt")
    public ResponseEntity<HashMap<String, Object>> validateToken(HttpServletRequest request, Authentication authentication) throws SQLException {

        response.reset()
                .setMessage("Token is Valid")
                .setURI(request.getRequestURI())
                .setResponseCode(HttpServletResponse.SC_OK)
                .addField("id",authentication.getName())
                .addField("authorities",authentication.getAuthorities());

        return response.getResponseEntity();

    }
}
