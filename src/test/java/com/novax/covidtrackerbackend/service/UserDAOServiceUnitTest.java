package com.novax.covidtrackerbackend.service;

import com.novax.covidtrackerbackend.model.dao.UserDAO;
import com.novax.covidtrackerbackend.repository.UserDAORepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserDAOServiceUnitTest {

    @Mock private UserDAORepository userDAORepository;
    private UserDAOService underTest;

    @BeforeEach
    void setUp(){
        underTest = new UserDAOService(userDAORepository);
    }

    @Test
    void isAbleToLoadUserByNicHospitalId() {
        UserDAO usr = new UserDAO();

        given(userDAORepository.findByNicAndHospitalusersHospital(anyString(),anyInt()))
                .willReturn(Optional.of(usr));

        //when
        UserDAO usrDAO = underTest.loadUserByNicHospitalId(anyString(),anyInt());

        //then
        verify(userDAORepository).findByNicAndHospitalusersHospital(anyString(),anyInt());
    }

    @Test
    void isNotAbleToLoadUserByNicHospitalId() {

        given(userDAORepository.findByNicAndHospitalusersHospital(anyString(),anyInt()))
                .willReturn(Optional.empty());
        assertThatThrownBy(() -> {
            //when
            UserDAO usrDAO = underTest.loadUserByNicHospitalId(anyString(),anyInt());
        })
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void isAbleToLoadUserByEmailNHospitalId() {
        UserDAO usr = new UserDAO();

        given(userDAORepository.findByEmailAndHospitalusersHospital(anyString(),anyInt()))
                .willReturn(Optional.of(usr));

        //when
        UserDAO usrDAO = underTest.loadUserByEmailNHospitalId(anyString(),anyInt());
        //then
        verify(userDAORepository).findByEmailAndHospitalusersHospital(anyString(),anyInt());
    }

    @Test
    void isNotAbleToLoadUserByEmailHospitalId() {

        given(userDAORepository.findByEmailAndHospitalusersHospital(anyString(),anyInt()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            //when
            UserDAO usrDAO = underTest.loadUserByEmailNHospitalId(anyString(),anyInt());
        })
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found");
    }
}