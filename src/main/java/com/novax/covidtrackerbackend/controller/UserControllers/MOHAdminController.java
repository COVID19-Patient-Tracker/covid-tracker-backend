package com.novax.covidtrackerbackend.controller.UserControllers;

import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(path = "management/api/V1/MOH/admin")
@PreAuthorize("hasAnyRole('ROLE_MOH_ADMIN,ROLE_HOSPITAL_ADMIN')")
public class MOHAdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

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
     * ADDS NEW MOH_USER/HOSPITAL_ADMIN
     * @param user - User entity to persist in the database.Refer user entity in the model.
     * @return - If database validation passes, returns copy of persisted user information as JSON object.
     */

    @PutMapping("/user/add")
    @PreAuthorize("hasAuthority('moh_admin:write')")
    public ResponseEntity<Optional<User>> addUser(@Valid @RequestBody User user) throws Exception {
        // TODO: sends an error if try to add different user type excep MOH_USER/HOSPITAL_ADMIN
        // TODO: Auto generate a password
        user.setPassword(passwordEncoder.encode("password")); // temporarily set password

        // TODO: send an email with password for newly saved user

        Optional<User> u = userService.addUser(user);
        ResponseEntity<Optional<User>> userResponseEntity = new ResponseEntity<>(u, HttpStatus.OK);
        return userResponseEntity;

    }

    /**
     * UPDATES INFORMATION OF MOH_ADMIN
     * @return
     */

    @PostMapping("/user/update")
    @PreAuthorize("hasAuthority('moh_admin:read')")
    public String getAllMOHUsers() {

        // buisness logic

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
