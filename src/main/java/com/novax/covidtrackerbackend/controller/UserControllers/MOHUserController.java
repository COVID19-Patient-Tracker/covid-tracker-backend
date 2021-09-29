package com.novax.covidtrackerbackend.controller.UserControllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.novax.covidtrackerbackend.model.Hospital;
import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.HospitalService;
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
 * This controller provides resources to the MOH user
 */

@RestController
@RequestMapping(path = "management/api/V1/MOH/user")
@PreAuthorize("hasRole('ROLE_MOH_USER')")
public class MOHUserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final HospitalService hospitalService;
    private final Response response;

    @Autowired
    public MOHUserController(UserService userService, PasswordEncoder passwordEncoder,HospitalService hospitalService,Response response) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.hospitalService = hospitalService;
        this.response = response;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('hospital_admin:read')")
    public String getAllMOHUsers(){
        // business logic
        return "HI All Users";
    }
    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_NEWROLE')")
    // @PreAuthorize("hasAuthority('moh_user:read')")
    // @PreAuthorize("hasAuthority('moh_user:read')")
    // @PreAuthorize("hasAuthority('moh_user:read')")

    /**
     *
     * @param hospital - Hospital entity to persist in the database. Refer Hospital entity in the modle
     * @param request - HttpServletRequest object to access uri
     * @return - If validation passes for all fields , returns added hospital as a JSON response
     */
    @PutMapping("/hospital/add")
    @PreAuthorize("hasAnyRole('MOH_USER')")
    public ResponseEntity<HashMap<String, Object>> addHospital(@Valid @RequestBody Hospital hospital,HttpServletRequest request){
        Hospital h = hospitalService.save(hospital);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("hospitalInfo",h);

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
    @PreAuthorize("hasAnyRole('MOH_USER')")
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
     * DELETES HOSPITAL_ADMIN/HOSPITAL_USER
     * @param u_id - User id to delete from the database.Refer user entity in the model.
     * @param request - HttpServletRequest object to access uri
     * @return - If user exist and successfully deleted, returns success message with deleted user_id.
     */

    @DeleteMapping("/delete/{u_id}")
    @PreAuthorize("hasAuthority('hospital_admin:write')")
    public ResponseEntity<HashMap<String, Object>> deleteUser(@PathVariable Long u_id,HttpServletRequest request) throws SQLException {
        Optional<User> user = userService.getUserById(u_id);
        if(user.isPresent()){

            User u = user.get();
            if(u.getRole().equals("HOSPITAL_ADMIN") || u.getRole().equals("HOSPITAL_USER")){

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
     * ADDS NEW HOSPITAL_ADMIN/HOSPITAL_USER
     * @param user - User entity to persist in the database.Refer user entity in the model.
     * @param request - HttpServletRequest object to access uri
     * @return - If database validation passes, returns copy of persisted user information as JSON object.
     */

    @PutMapping("/user/add")
    @PreAuthorize("hasAuthority('hospital_admin:write')")
    @JsonView(User.WithoutPasswordView.class)
    public ResponseEntity<HashMap<String, Object>> addUser(@Valid @RequestBody User user,HttpServletRequest request){
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
                .addField("userInfo",useWithOutPasswordView.getUserDetails());
        return response.getResponseEntity();
    }

    /**
     * DELETES HOSPITAL
     * @param hospitalId - id of the hospital to delete
     * @param request - HttpServletRequest object to access uri
     * @return Response object if success or in exception
     */

    @DeleteMapping("/hospital/delete/{hospitalId}")
    @PreAuthorize("hasAnyRole('MOH_USER')")
    public ResponseEntity<HashMap<String, Object>> deleteHospital(@PathVariable("hospitalId") int hospitalId, HttpServletRequest request){
        // exceptions handled
        hospitalService.deleteHospitalById(hospitalId);

        // if no exception occurred send this response
        response.reset().setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI());

        return response.getResponseEntity();
    }

}
