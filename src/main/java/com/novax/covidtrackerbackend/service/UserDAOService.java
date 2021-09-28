package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.dao.UserDAO;
import com.novax.covidtrackerbackend.repository.UserDAORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserDAOService {

    @Autowired
    private final UserDAORepository userDAORepository;

    public UserDAO loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<UserDAO> result = userDAORepository.findByEmail(userEmail);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }
}

