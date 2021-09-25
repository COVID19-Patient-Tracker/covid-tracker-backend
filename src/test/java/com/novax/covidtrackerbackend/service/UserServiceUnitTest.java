package com.novax.covidtrackerbackend.service;
import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock private UserRepository userRepository;
    private UserService underTest;


    @BeforeEach
    void setUp(){
        underTest = new UserService(userRepository);
    }

    @Test
    void isAbleToGetAllUsers() {
        // when
        List<User> allUsers = underTest.getAllUsers();
        // then
        verify(userRepository).findAll();
    }

    @Test
    @Disabled
    void isUserExist() {
    }

    @Test
    @Disabled
    void getUserByEmail() {
    }

    // see about argument captor
}
