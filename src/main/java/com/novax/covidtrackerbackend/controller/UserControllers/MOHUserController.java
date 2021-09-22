package com.novax.covidtrackerbackend.controller.UserControllers;

import com.novax.covidtrackerbackend.model.Hospital;
import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.HospitalService;
import com.novax.covidtrackerbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Optional;

/**
 * This controller provides resources to the MOH user
 *
 * @return
 */

@RestController
@RequestMapping(path = "management/api/V1/MOH/user")
@PreAuthorize("hasRole('ROLE_MOH_USER')")
public class MOHUserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final HospitalService hospitalService;
    @Autowired
    public MOHUserController(UserService userService, PasswordEncoder passwordEncoder,HospitalService hospitalService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.hospitalService = hospitalService;
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
     * @param hospital - Hospital entity to pserdidt in the databse. Refer Hospital entity in the modle
     * @return - If validation passes for all fields , returns added hospital as a JSON response
     */

    @PutMapping("/hospital/add")
    @PreAuthorize("hasAnyRole('MOH_USER')")
    public ResponseEntity<HashMap<String, Object>> addHospital(@Valid @RequestBody Hospital hospital,HttpServletRequest request){
        Hospital h = hospitalService.save(hospital);
        // if no exception occurred send this response
        Response<Object> response = new Response<>();
        response.setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("hospitalInfo",h);
        return response.getResponseEntity();
    }

    /**
     * ADDS NEW HOSPITAL_ADMIN/HOSPITAL_USER
     * @param user - User entity to persist in the database.Refer user entity in the model.
     * @return - If database validation passes, returns copy of persisted user information as JSON object.
     */

    @PutMapping("/user/add")
    @PreAuthorize("hasAuthority('moh_user:write')")
    public ResponseEntity<HashMap<String, Object>> addUser(@Valid @RequestBody User user,HttpServletRequest request) throws Exception {
        // TODO: sends an error if try to add different user type excep HOSPITAL_USER/HOSPITAL_ADMIN
        // TODO: Auto generate a password
        user.setPassword(passwordEncoder.encode("password")); // temporarily set password

        // TODO: send an email with password for newly saved user

        Optional<User> u = userService.addUser(user);
        // if no exception occurred send this response
        Response<Object> response = new Response<>();
        response.setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("userInfo",u);
        return response.getResponseEntity();
    }

    /**
     *
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
        Response<Object> response = new Response<>();
        response.setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI());

        return response.getResponseEntity();
    }


}
