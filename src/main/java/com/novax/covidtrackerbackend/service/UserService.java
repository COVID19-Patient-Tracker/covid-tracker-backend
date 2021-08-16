package com.novax.covidtrackerbackend.service;

import java.util.List;
import java.util.Optional;

import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserRepository userRepository;
    
    public List<User> getAllUsers(){
        return (List<User>) userRepository.findAll();
    }

    public boolean isUserExist(String email) {
        return userRepository.isUserExist(email);
    }

    public Optional<User> getUserByEmail(String email){
        Optional<User> user = userRepository.getUserByEmail(email);
        return user;
    }

}
