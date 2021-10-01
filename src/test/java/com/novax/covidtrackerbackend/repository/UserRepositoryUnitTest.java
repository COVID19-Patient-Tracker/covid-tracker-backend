package com.novax.covidtrackerbackend.repository;

import com.novax.covidtrackerbackend.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.SQLException;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryUnitTest {

    @Autowired
    private UserRepository underTest;

    @BeforeEach
    void init() {
        underTest.deleteAll();
    }
    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldGetUserByEmail() {
        // given
        User user = new User(
                null,
                "abcdefgh789",
                "mohadmn_new@gmail.com",
                "f_name",
                "l_name",
                "MOH_ADMIN",
                "999999999v",
                99
                );

        underTest.save(user);

        // when
        Optional<User> exists = underTest.getUserByEmail("mohadmn_new@gmail.com");

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
                null,
                "password",
                "mohadmn@gmail.com",
                "f_name",
                "l_name",
                "MOH_ADMIN",
                "999999999v",
                99
        );

        underTest.save(user);

        // when
        Integer exists = underTest.isUserExist("mohadmn@gmail.com");

        // then
        assertThat(exists).isEqualTo(1);

    }

    @Test
    void itShouldReturnFalseIfEmailIsNull(){
        String nullemail = null;

        // when
        Integer emptyemail = underTest.isUserExist(nullemail);

        // then
        assertThat(emptyemail).isEqualTo(0);
    }

    @Test
    void itShouldReturnFalseIfEmailIsWrong(){
        String wrongemail = "wrong";
        // when
        Integer notExists = underTest.isUserExist(wrongemail);

        // then
        assertThat(notExists).isEqualTo(0);
    }
    @Test
//    @Sql({"/create_stored_procedure.sql"})
    void itShouldAddMOHAdmin() {
        User user = new User(
                null,
                "pwdpwd",
                "mohadmn_usr@gmail.com",
                "f_name",
                "l_name",
                "MOH_ADMIN",
                "999999999v",
                0
        );

        Optional<User> output = underTest.addUser(
                user.getEmail(),user.getRole(),user.getNic(),user.getPassword(),user.getFirst_name(),user.getLast_name(),user.getHospital_id()
        );

        User expectedUser = output.get();
        user.setUser_id(expectedUser.getUser_id());
        assertThat(expectedUser).isEqualTo(user);
        assertThatExceptionOfType(SQLException.class);

    }
}