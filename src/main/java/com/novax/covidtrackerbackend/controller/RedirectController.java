package com.novax.covidtrackerbackend.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class RedirectController {
    
    @GetMapping(path = "/redirect")
    public String redirectAfterLogin(HttpServletRequest request){
        if(request.isUserInRole("ROLE_ADMIN")){
            return "redirect: /admin/";
        }
        else if (request.isUserInRole("ROLE_MOH_ADMIN")) {
            return "redirect: /MOH/admin";
        }
        else if (request.isUserInRole("ROLE_MOH_USER")) {
            return "redirect: /MOH/user";
        }
        else if (request.isUserInRole("ROLE_PATIENT")) {
            return "redirect: /public/user";
        }
        else if (request.isUserInRole("ROLE_HOSPITAL_USER")) {
            return "redirect: /hospital/user";
        }
        else if (request.isUserInRole("ROLE_HOSPITAL_ADMIN")) {
            return "redirect: /hospital/admin";
        }
        return "redirect: /login/";
    }
}
