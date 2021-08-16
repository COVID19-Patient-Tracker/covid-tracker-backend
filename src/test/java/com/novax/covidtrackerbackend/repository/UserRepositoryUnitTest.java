package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryUnitTest {

    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldGetUserByEmail() {
        // given
        User user = new User(
                999999999999999990L,
                "MOH_ADMIN",
                "encrypted_pwd",
                "mohadmn@gmail.com"
        );

        underTest.save(user);

        // when
        Optional<User> exists = underTest.getUserByEmail("mohadmn@gmail.com");

        // then
        assertThat(exists).isNotEmpty();
        assertThat(exists).hasValue(user);


    }

    @Test
    void itShouldReturnNullIfEmailIsWrong(){
        String email = "wrongemail";
        // when
        Optional<User> wrongemail = underTest.getUserByEmail(email);

        // then
        assertThat(wrongemail).isEmpty();
    }

    @Test
    void itShouldReturnNullIfEmailIsNull(){
        String email = "";
        // when
        Optional<User> emptyemail = underTest.getUserByEmail(email);

        // then
        assertThat(emptyemail).isEmpty();
    }

    @Test
    void itShouldCheckIfIsUserExist() {
        // given
        User user = new User(
                999999999999999990L,
                "MOH_ADMIN",
                "encrypted_pwd",
                "mohadmn@gmail.com"
        );

        underTest.save(user);

        // when
        boolean exists = underTest.isUserExist("mohadmn@gmail.com");

        // then
        assertThat(exists).isEqualTo(true);

    }

    @Test
    void itShouldReturnFalseIfEmailIsNull(){
        String nullemail = null;

        // when
        boolean emptyemail = underTest.isUserExist(nullemail);

        // then
        assertThat(emptyemail).isEqualTo(false);
    }

    @Test
    void itShouldReturnFalseIfEmailIsWrong(){
        String wrongemail = "wrong";
        // when
        boolean notExists = underTest.isUserExist(wrongemail);

        // then
        assertThat(notExists).isEqualTo(false);
    }
}