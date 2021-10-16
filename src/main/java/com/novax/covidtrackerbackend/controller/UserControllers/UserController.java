package com.novax.covidtrackerbackend.controller.UserControllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserController(UserService userService, Response response) {
        this.userService = userService;
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
}
