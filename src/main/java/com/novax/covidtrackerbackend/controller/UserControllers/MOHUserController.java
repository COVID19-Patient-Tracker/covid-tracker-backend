package com.novax.covidtrackerbackend.controller.UserControllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "management/api/V1/MOH/user")
@PreAuthorize("hasRole('ROLE_MOH_USER')")
public class MOHUserController {
    
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
}
