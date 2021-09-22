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

    @Autowired
    public MOHUserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('hospital_admin:read')")
    public String getAllMOHUsers(){
        // business logic
        return "HI All Users";
    }


    @PutMapping("/hospital/add")
    @PreAuthorize("hasAnyAuthority('moh_user:write')")
    public String addHospital(){
        // adding a hospital
        // business logic
        return "hospital added";
    }

    /**
     * ADDS NEW HOSPITAL_ADMIN/HOSPITAL_USER
     * @param user - User entity to persist in the database.Refer user entity in the model.
     * @return - If database validation passes, returns copy of persisted user information as JSON object.
     */

    @PutMapping("/user/add")
    @PreAuthorize("hasAuthority('moh_user:write')")
    public ResponseEntity<Optional<User>> addUser(@Valid @RequestBody User user) throws Exception {
        // TODO: sends an error if try to add different user type excep HOSPITAL_USER/HOSPITAL_ADMIN
        // TODO: Auto generate a password
        user.setPassword(passwordEncoder.encode("password")); // temporarily set password

        // TODO: send an email with password for newly saved user

        Optional<User> u = userService.addUser(user);
        ResponseEntity<Optional<User>> userResponseEntity = new ResponseEntity<>(u, HttpStatus.OK);
        return userResponseEntity;

    }


//    @DeleteMapping("/hospital/delete")
//    @PreAuthorize("hasAnyAuthority('moh_admin:write')")
//    public String deleteHospital(){
//        // deleting a hospital
//        // business logic
//        return "hospital deleted";
//    }

    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_NEWROLE')")
    // @PreAuthorize("hasAuthority('moh_user:read')")
    // @PreAuthorize("hasAuthority('moh_user:read')")
    // @PreAuthorize("hasAuthority('moh_user:read')")
}
