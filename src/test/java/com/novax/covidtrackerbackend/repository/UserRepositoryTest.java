package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Test
    void itShouldGetUserByEmail() {
        // given
        User user = new User(
                90000,
                "MOH_ADMIN",
                "encrypted_pwd",
                "mohadmn@gmail.com"
        );
        underTest.save(user);

        // when
        Optional<User> exists = underTest.getUserByEmail("mohadmn@gmail.com");

        // then
        assertThat(exists).isEqualTo(user);
    }

    @Test
    void isUserExist() {
    }
}