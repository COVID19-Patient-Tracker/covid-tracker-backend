package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.dao.UserDAO;
import com.novax.covidtrackerbackend.repository.UserDAORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserDAOService {

    @Autowired
    private final UserDAORepository userDAORepository;

    /**
     * GETS USER BY NIC AND HOSPITAL ID
     * @param userNic - NIc of the user
     * @param hospitalId - user hospital id
     * @return UserDAO object
     */
    public UserDAO loadUserByNicHospitalId(String userNic, Integer hospitalId)  {
        Optional<UserDAO> result = userDAORepository.findByNicAndHospitalusersHospital(userNic , hospitalId);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    public UserDAO loadUserByHospitalId(Integer hospitalId)  {
        Optional<UserDAO> result = userDAORepository.findByHospitalusersHospital(hospitalId);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    /**
     * GETS USER BY HOSPITAL ID AND EMAIL
     * @param userEmail - Email of the user
     * @param hospitalId - user hospital id
     * @return UserDAO object
     */
    public UserDAO loadUserByEmailNHospitalId(String userEmail, Integer hospitalId) {
        Optional<UserDAO> result = userDAORepository.findByEmailAndHospitalusersHospital(userEmail, hospitalId);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    /**
     * GETS ALL USERS BY HOSPITAL ID AND ROLE
     * @param userRole - Role of the user
     * @param hospitalId - user hospital id
     * @return UserDAO object
     */
    public List<UserDAO> loadUsersByRoleNHospitalId(String userRole, Integer hospitalId) {
        return userDAORepository.findByRoleAndHospitalusersHospital(userRole, hospitalId);
    }
}

