package com.novax.covidtrackerbackend.controller.UserControllers;

import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.model.dao.UserDAO;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.UserDAOService;
import com.novax.covidtrackerbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("management/api/V1/hospital/admin")
@PreAuthorize("hasAnyRole('ROLE_MOH_ADMIN,ROLE_HOSPITAL_ADMIN')")
public class HospitalAdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDAOService userDAOService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/user/add")
    @PreAuthorize("hasAnyRole('HOSPITAL_ADMIN')")
    public ResponseEntity<HashMap<String, Object>> addUser(@Valid @RequestBody User user, HttpServletRequest request) throws IOException {

        user.setPassword(passwordEncoder.encode("password")); // temporarily set password
        Optional<User> new_user = userService.addUser(user);

        Response<Object> response = new Response<>();
        response.setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("userInfo", new_user);
        return response.getResponseEntity();
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<HashMap<String, Object>> getUserByEmail( @PathVariable("email") String userEmail){
        try{
            UserDAO user = (UserDAO) userDAOService.loadUserByUsername(userEmail);
            Response<Object> response = new Response<>();
            response.setResponseCode(HttpStatus.OK.value())
                    .addField("userInfo", user);
            return response.getResponseEntity();

        } catch (EntityNotFoundException e) {
            System.out.println("Error");
            Response<Object> response = new Response<>();
            response.setResponseCode(HttpStatus.NOT_FOUND.value())
                    .setException(e);
            return response.getResponseEntity();
        }
    }
}
