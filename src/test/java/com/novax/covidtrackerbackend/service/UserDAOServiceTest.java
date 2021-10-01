package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.dao.UserDAO;
import com.novax.covidtrackerbackend.repository.UserDAORepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserDAOServiceTest {

    @Mock
    UserDAORepository userDAORepository;

    UserDAOService userDAOService;

    @BeforeEach
    void setUp() {
        userDAOService = new UserDAOService(userDAORepository);
    }

    //mock data
    String nic = "976568767v";
    Integer hospitalId = 1;
    String email ="email@gmail.com";
    UserDAO user = new UserDAO(100700L, "HOSPITAL_ADMIN", "password", "email@gmail.com", "Roshinie", "Jayasundara", "nic" );

    //Exceptions
    String userNotFoundException = "User not found";

    @Test
    @DisplayName("Get User By NIC and hospital ID Success")
    void loadUserByNicHospitalIdReturnsUser() {
        Mockito.when(userDAORepository.findByNicAndHospitalusersHospital(nic , hospitalId)).thenReturn(Optional.of(user));
        UserDAO result = userDAOService.loadUserByNicHospitalId(nic , hospitalId);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("Get User By NIC and hospital ID Returns user not found exception")
    void loadUserByNicHospitalIdReturnNotFound() {
        Mockito.when(userDAORepository.findByNicAndHospitalusersHospital(nic , hospitalId)).thenReturn(Optional.empty());
        Exception thrown = assertThrows(EntityNotFoundException.class, () -> {
            userDAOService.loadUserByNicHospitalId(nic, hospitalId);
        });
       assertEquals(thrown.getMessage(), userNotFoundException);
    }

    @Test
    @DisplayName("Get User By Email and hospital ID Success")
    void loadUserByEmailNHospitalIdReturnsUser() {
        Mockito.when(userDAORepository.findByEmailAndHospitalusersHospital(email , hospitalId)).thenReturn(Optional.of(user));
        UserDAO result = userDAOService.loadUserByEmailNHospitalId(email , hospitalId);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("Get User By Email and hospital ID Returns user not found exception")
    void loadUserByEmailNHospitalIdReturnNotFoundException() {
        Mockito.when(userDAORepository.findByEmailAndHospitalusersHospital(email , hospitalId)).thenReturn(Optional.empty());
        Exception thrown = assertThrows(EntityNotFoundException.class, () -> {
            userDAOService.loadUserByEmailNHospitalId(email , hospitalId);
        });
        assertEquals(thrown.getMessage(), userNotFoundException);
    }
}