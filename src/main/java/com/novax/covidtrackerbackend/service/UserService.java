package com.novax.covidtrackerbackend.service;

import java.util.List;
import java.util.Optional;

import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    public Optional<User> addUser(User user){
        Optional<User> u = userRepository.addUser(
                user.getEmail(),
                user.getRole(),
                user.getNic(),
                user.getPassword(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getHospital_id()
        );
        return u;
    }
}
