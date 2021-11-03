package com.novax.covidtrackerbackend.controller.UserControllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.model.dao.UserDAO;
import com.novax.covidtrackerbackend.response.Response;
import com.novax.covidtrackerbackend.service.UserDAOService;
import com.novax.covidtrackerbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
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

    /**
     * ADDS NEW HOSPITAL USER OR ADMIN
     * @param user - User entity to persist in the database.Refer user entity in the model.
     * @return - If database validation passes, returns copy of persisted user information as JSON object.
     */

    @PostMapping("/user/add")
    @PreAuthorize("hasAnyRole('HOSPITAL_ADMIN')")
    public ResponseEntity<HashMap<String, Object>> addUser(@Valid @RequestBody User user, HttpServletRequest request) throws IOException {

        user.setPassword(passwordEncoder.encode("password")); // temporarily set password
        Optional<User> new_user = userService.addUser(user);
        Response response = new Response();
        response.setResponseCode(HttpStatus.OK.value())
                .setMessage("request success")
                .setURI(request.getRequestURI())
                .addField("userInfo", new_user);
        return response.getResponseEntity();
    }

    /**
     * GET
     * @param userNic  - NIC of the requested user
     * @param hospitalId  - user accessed hospital id setted automatically
     * @return - If user exists under the given condition user details will be returned
     * @throws EntityNotFoundException if the user is not available in the database
     */

    @GetMapping("/user/{hospitalId}/nic/{userNic}")
    @PreAuthorize("hasAnyRole('HOSPITAL_ADMIN')")
    public ResponseEntity<HashMap<String, Object>> getUserByNicAndHospitalID(@PathVariable("userNic") String userNic,
                                                                             @PathVariable("hospitalId") Integer hospitalId,
                                                                             HttpServletRequest request) {
        try {
            UserDAO user = userDAOService.loadUserByNicHospitalId(userNic, hospitalId);
            Response response = new Response();
            response.setResponseCode(HttpStatus.OK.value())
                    .addField("userInfo", user);
            return response.getResponseEntity();

        } catch (EntityNotFoundException e) {
            Response response = new Response();
            response.setResponseCode(HttpStatus.NOT_FOUND.value())
                    .setException(e)
                    .setMessage("Requested User Not Found")
                    .setURI(request.getRequestURI());
            return response.getResponseEntity();
        }
    }

    @GetMapping("/user/{hospitalId}")
    @PreAuthorize("hasAnyRole('HOSPITAL_ADMIN')")
    public ResponseEntity<HashMap<String, Object>> getUserByHospitalID(@PathVariable("hospitalId") Integer hospitalId,
                                                                             HttpServletRequest request) {
        try {
            UserDAO user = userDAOService.loadUserByHospitalId(hospitalId);
            Response response = new Response();
            response.setResponseCode(HttpStatus.OK.value())
                    .addField("userInfo", user);
            return response.getResponseEntity();

        } catch (EntityNotFoundException e) {
            Response response = new Response();
            response.setResponseCode(HttpStatus.NOT_FOUND.value())
                    .setException(e)
                    .setMessage("Requested User Not Found")
                    .setURI(request.getRequestURI());
            return response.getResponseEntity();
        }
    }

    /**
     * GET
     * @param userEmail  - Email of the requested user
     * @param hospitalId  - user accessed hospital id setted automatically
     * @return - If user exists under the given condition user details will be returned
     * @throws EntityNotFoundException if the user is not available in the database
     */

    @GetMapping("/user/{hospitalId}/email/{userEmail}")
    @PreAuthorize("hasAnyRole('HOSPITAL_ADMIN')")
    public ResponseEntity<HashMap<String, Object>> getUserByEmailAndHospitalID(@PathVariable("userEmail") String userEmail,
                                                                               @PathVariable("hospitalId") Integer hospitalId,
                                                                               HttpServletRequest request) {
        try {
            UserDAO user = userDAOService.loadUserByEmailNHospitalId(userEmail, hospitalId);
            Response response = new Response();
            response.setResponseCode(HttpStatus.OK.value())
                    .addField("userInfo", user);
            return response.getResponseEntity();

        } catch (EntityNotFoundException e) {
            Response response = new Response();
            response.setResponseCode(HttpStatus.NOT_FOUND.value())
                    .setException(e)
                    .setMessage("Requested User Not Found")
                    .setURI(request.getRequestURI());
            return response.getResponseEntity();
        }
    }

    @DeleteMapping("/delete/{u_id}")
    @PreAuthorize("hasAuthority('hospital_user:write')")
    @JsonView(User.OnlyEmailNicRoleAndIdView.class)
    public ResponseEntity<HashMap<String, Object>> deleteUser(@PathVariable Long u_id,HttpServletRequest request) throws SQLException {
        Optional<User> user = userService.getUserById(u_id);
        Response response = new Response();
        if(user.isPresent()){

            User u = user.get();
            if(u.getRole().equals("HOSPITAL_ADMIN") || u.getRole().equals("HOSPITAL_USER")){
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
}
