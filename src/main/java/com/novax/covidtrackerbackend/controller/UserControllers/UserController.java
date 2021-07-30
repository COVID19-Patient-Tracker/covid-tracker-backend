package com.novax.covidtrackerbackend.controller.UserControllers;

import java.util.List;

import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "app/V1/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping(path = "/dashboard")
    public User getUser() throws Exception{
        return userService.getUserByEmail("abcdfgh@gmail.com").orElseThrow(() ->  new Exception("User not found"));
    }

    @GetMapping(path = "/AllUsers")
    public List<User> getAllUsers(){
        System.out.println(userService.getAllUsers());
        return userService.getAllUsers();
    }
}
