package com.novax.covidtrackerbackend.controller.UserControllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

/**
 * this controller provides resources for the MOH admin
 */
@RestController
@RequestMapping(path = "management/api/V1/MOH/admin")
@PreAuthorize("hasAnyRole('ROLE_MOH_ADMIN')")
public class MOHAdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private MappingJacksonValue value;
    private final Response response;

    @Autowired
    public MOHAdminController(UserService userService, PasswordEncoder passwordEncoder,Response response) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.response = response;
    }

    /**
     * DASHBOARD RELATED DATA
     * @return - returns copy of persisted user information as JSON object.
     */

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('moh_admin:read')")
    public String getHospitalDetails() {

        // business logic

        return "dashboard of the MOH";
    }





    /**
     * UPDATES INFORMATION OF MOH_ADMIN
     * @return
     */

    @PostMapping("/user/update")
    @PreAuthorize("hasAuthority('moh_admin:read')")
    public String getAllMOHUsers() {
        return "dashboard of the MOH";
    }

    /**
     * DELETES MOH_USER/HOSPITAL_ADMIN
     * @param u_id - NIC of the targeted user to delete.
     * @return - If database validation passes, returns copy of deleted user information as JSON object.
     */
    @DeleteMapping("/delete/{u_id}")
    @PreAuthorize("hasAuthority('hospital_admin:write')")
    public ResponseEntity<HashMap<String, Object>> deleteUser(@PathVariable Long u_id,HttpServletRequest request) throws SQLException {

        Optional<User> user = userService.getUserById(u_id);

        if(user.isPresent()){

            User u = user.get();

            if(u.getRole().equals("MOH_ADMIN") || u.getRole().equals("MOH_USER")){

                // if no exception occurred send this response
                response.setResponseCode(HttpStatus.OK.value())
                        .setMessage("request success")
                        .setURI(request.getRequestURI())
                        .addField("Deleted",u);
                userService.deleteUser(u_id);

            }else{
                throw new EmptyResultDataAccessException("You don't have permission to delete this type of users",0);
            }
        }

        return response.getResponseEntity();
    }

    /**
     *
     * @param newDetailsOfUser - updated details sent by the client
     * @param request - HttpServletRequest object to access uri
     * @param auth - Authentication object in the application context to access authenticated user's user_id
     * @return - updated user details with response
     * @throws SQLException
     */
    @PostMapping("/update/details")
    @PreAuthorize("hasAnyRole('ROLE_MOH_ADMIN')")
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
     * ADDS NEW MOH_USER/HOSPITAL_ADMIN
     * @param user - User entity to persist in the database.Refer user entity in the model.
     * @return - If database validation passes, returns copy of persisted user information as JSON object.
     */

    @PutMapping("/user/add")
    @PreAuthorize("hasAuthority('moh_admin:write')")
    public ResponseEntity<HashMap<String, Object>> addUser(@Valid @RequestBody User user, HttpServletRequest request) throws Exception {
        // TODO: sends an error if try to add different user type excep HOSPITAL_USER/HOSPITAL_ADMIN
        // TODO: Auto generate a password
        user.setPassword(passwordEncoder.encode("password")); // temporarily set password

        Optional<User> u = userService.addUser(user);

        // exclude unwanted details (pw)
        MappingJacksonValue value = new MappingJacksonValue(u.get());
        value.setSerializationView(User.WithoutPasswordView.class);
        User useWithOutPasswordView = (User)  value.getValue();

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("Info",useWithOutPasswordView.getUserDetails());

        return response.getResponseEntity();
    }

    @PostMapping("/user/update/details")
    @PreAuthorize("hasAuthority('moh_admin:write')")
    public String updateMOHUser() {
        // updating details of a user
        // business logic
        return "user details updated";
    }


    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_NEWROLE')")
    // @PreAuthorize("hasAuthority('moh_user:read')")
    // @PreAuthorize("hasAuthority('moh_user:read')")
    // @PreAuthorize("hasAuthority('moh_user:read')")
}
