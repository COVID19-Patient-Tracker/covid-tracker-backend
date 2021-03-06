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
     * DELETES MOH_USER/HOSPITAL_ADMIN
     * @param u_id - NIC of the targeted user to delete.
     * @return - If database validation passes, returns copy of deleted user information as JSON object.
     */

    @DeleteMapping("/delete/{u_id}")
    @PreAuthorize("hasAnyAuthority('hospital_admin:write,moh_admin:write')")
    @JsonView(User.OnlyEmailNicRoleAndIdView.class)
    public ResponseEntity<HashMap<String, Object>> deleteUser(@PathVariable Long u_id,HttpServletRequest request) throws SQLException {

        Optional<User> user = userService.getUserById(u_id);

        if(user.isPresent()){
            User u = user.get();
            if(u.getRole().equals("MOH_ADMIN") || u.getRole().equals("MOH_USER")){
                userService.deleteUser(u_id);

                MappingJacksonValue value = new MappingJacksonValue(u);
                value.setSerializationView(User.OnlyEmailNicRoleAndIdView.class);
                User onlyEmailNicRoleAndIdView = (User)  value.getValue();



                // if no exception occurred send this response
                response.setResponseCode(HttpStatus.OK.value())
                        .setMessage("request success. user deleted")
                        .setURI(request.getRequestURI())
                        .addField("Deleted",onlyEmailNicRoleAndIdView);


            }else{
                throw new EmptyResultDataAccessException("You don't have permission to delete this type of users",0);
            }
        }
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

}
