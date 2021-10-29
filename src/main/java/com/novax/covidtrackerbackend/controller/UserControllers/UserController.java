package com.novax.covidtrackerbackend.controller.UserControllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonView;
import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.model.dto.PatientRegisterDTO;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequestMapping(path = "app/V1/user")
public class UserController {

    List<User> cacheAllUsers = null;


    private final UserService userService;
    private MappingJacksonValue value;
    private final Response response;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder, Response response) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.response = response;
    }

    @GetMapping(path = "/dashboard")
    public User getUser() throws Exception{
        return userService.getUserByEmail("abcdfgh@gmail.com").orElseThrow(() ->  new Exception("User not found"));
    }

    @GetMapping(path = "/AllUsers")
    public List<User> getAllUsers(){
        if(!((cacheAllUsers) == null)){
            return cacheAllUsers;
        }
        return cacheAllUsers = userService.getAllUsers();
    }

    /**
     * CHANGE PASSWORD OF THE MOH ADMIN
     * @param newDetailsOfUser - updated details sent by the client
     * @param request - HttpServletRequest object to access uri
     * @param auth - Authentication object in the application context to access authenticated user's user_id
     * @return - updated user details with response
     * @throws SQLException
     */

    @PostMapping("/update/password")
    @JsonView(User.WithoutPasswordView.class)
    public ResponseEntity<HashMap<String, Object>> updateMOHUserPassword(@Valid @RequestBody User newDetailsOfUser, HttpServletRequest request, Authentication auth) throws SQLException {

        User updatedDetailsOfUser = userService.updateUserPassword(newDetailsOfUser,auth);

        // send this response if data update is success
        // exclude unwanted details (pw)
        MappingJacksonValue value = new MappingJacksonValue(updatedDetailsOfUser);
        value.setSerializationView(User.WithoutPasswordView.class);
        User useWithOutPasswordView = (User)  value.getValue();

        response.reset()
                .setMessage("password updated")
                .setURI(request.getRequestURI())
                .setResponseCode(HttpServletResponse.SC_OK);

        return response.getResponseEntity();
    }

    /**
     * UPDATES DETAILS OF THE OWN
     * @param newDetailsOfUser - updated details sent by the client
     * @param request - HttpServletRequest object to access uri
     * @param auth - Authentication object in the application context to access authenticated user's user_id
     * @return - updated user details with response
     * @throws SQLException
     */

    @PostMapping("/update/details")
    @JsonView(User.WithoutPasswordView.class)
    public ResponseEntity<HashMap<String, Object>> updateMOHUserDetails(@Valid @RequestBody User newDetailsOfUser, HttpServletRequest request, Authentication auth) throws SQLException {

        User updatedDetailsOfUser = userService.updateUserDetails(newDetailsOfUser,auth);

        // send this response if data update is success
        // exclude unwanted details (pw)
        MappingJacksonValue value = new MappingJacksonValue(updatedDetailsOfUser);
        value.setSerializationView(User.WithoutPasswordView.class);
        User useWithOutPasswordView = (User)  value.getValue();

        response.reset()
                .setMessage("details updated")
                .setURI(request.getRequestURI())
                .setResponseCode(HttpServletResponse.SC_OK)
                .addField("updatedInfo",useWithOutPasswordView);

        return response.getResponseEntity();
    }

    /**
     * REGISTER HOSPITAL PATIENT TO THE SYSTEM
     * @param data - PatientRegisterDTO which consists of email,nic,password and patient_id.
     * @param request - HttpServletRequest object to access uri
     * @return - If the signup successful returns the user information.
     */

    @PostMapping("/signup")
    @JsonView(User.WithoutPasswordView.class)
    public ResponseEntity<HashMap<String, Object>> registerPatient(@Valid @RequestBody PatientRegisterDTO data, HttpServletRequest request){

        data.setPassword(passwordEncoder.encode(data.getPassword()));
        Optional<User> reg_user = userService.patientRegister(data);

        // exclude unwanted details (pw)
        MappingJacksonValue value = new MappingJacksonValue(reg_user.get());
        value.setSerializationView(User.WithoutPasswordView.class);
        User useWithOutPasswordView = (User)  value.getValue();

        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("Registered Successfully")
                .setURI(request.getRequestURI())
                .addField("userInfo",useWithOutPasswordView.getUserDetails());


    /**
     * GET DETAILS OF USER BY ROLE
     * @param role - role of the users requested
     * @param request - HttpServletRequest object to access uri
     * @param auth - Authentication object in the application context to access authenticated user's user_id
     * @return - updated user details with response
     * @throws SQLException
     */

    @GetMapping("/get/users/{role}")
    @JsonView(User.WithoutPasswordView.class)
    public ResponseEntity<HashMap<String, Object>> updateMOHUserDetails(@PathVariable("role") String role, HttpServletRequest request, Authentication auth) throws SQLException {

        List<User> allUsers = userService.getAllUsers();

        // filtering by role
        List<User> filteredUserByRole = new ArrayList<>();
        for (User user:
             allUsers) {
            if(user.getRole().equals(role)){
                // send this response if data update is success
                // exclude unwanted details (pw)
                MappingJacksonValue value = new MappingJacksonValue(user);
                value.setSerializationView(User.WithoutPasswordView.class);
                User useWithOutPasswordView = (User)  value.getValue();
                filteredUserByRole.add(useWithOutPasswordView);
            }
        }

        response.reset()
                .setMessage("all users by role")
                .setURI(request.getRequestURI())
                .setResponseCode(HttpServletResponse.SC_OK)
                .addField("users",filteredUserByRole);

        return response.getResponseEntity();
    }
}
