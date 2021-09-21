package com.novax.covidtrackerbackend.controller.UserControllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "management/api/V1/admin")
@PreAuthorize("hasAnyRole('ROLE_MOH_ADMIN,ROLE_HOSPITAL_ADMIN')")
public class AdminController {


    // dashboard related data returned by this method
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('moh_admin:read')")
    public String getHospitalDetails() {
        // business logic
        return "dashboard of the MOH";
    }

    // add new moh users
    @PutMapping("/user/add")
    @PreAuthorize("hasAuthority('moh_admin:write')")
    public String addUser() {
        // business logic
        return "user added to the database";
    }





}
