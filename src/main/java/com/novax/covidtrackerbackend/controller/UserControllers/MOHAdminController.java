package com.novax.covidtrackerbackend.controller.UserControllers;

import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(path = "management/api/V1/MOH/admin")
@PreAuthorize("hasAnyRole('ROLE_MOH_ADMIN,ROLE_HOSPITAL_ADMIN')")
public class MOHAdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private MappingJacksonValue value;

    @Autowired
    public MOHAdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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
     * @param nic - NIC of the targeted user to delete.
     * @return - If database validation passes, returns copy of deleted user information as JSON object.
     */

    @DeleteMapping("/user/delete")
    @PreAuthorize("hasAuthority('moh_admin:write')")
    public String deleteMOHUser(@RequestBody String nic){

        // deleting a user
        // business logic

        return "user deleted";
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
        Response response = new Response();
        response.setResponseCode(HttpStatus.OK.value())
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
