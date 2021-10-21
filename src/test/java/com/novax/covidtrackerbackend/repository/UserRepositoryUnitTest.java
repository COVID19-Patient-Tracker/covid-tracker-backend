package com.novax.covidtrackerbackend.repository;
import com.novax.covidtrackerbackend.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaSystemException;
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

    // get user by email
    @Test
    @DisplayName("get user by email")
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

    // what if email is wrong
    @Test
    @DisplayName("should return null if email is wrong")
    void itShouldReturnNullIfEmailIsWrong(){
        String email = "wrongemail";
        // when
        Optional<User> wrongemail = underTest.getUserByEmail(email);

        // then
        assertThat(wrongemail).isEmpty();
    }

    // what if email is null
    @Test
    @DisplayName("should return null if email is null")
    void itShouldReturnNullIfEmailIsNull(){
        String email = "";
        // when
        Optional<User> emptyemail = underTest.getUserByEmail(email);

        // then
        assertThat(emptyemail).isEmpty();
    }

    // existence of record of user using email
    @Test
    @DisplayName("user exists by email")
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
                  0
        );

        underTest.save(user);

        // when
        Integer exists = underTest.isUserExist("mohadmn@gmail.com");

        // then
        assertThat(exists).isEqualTo(1);

    }

    @Test
    @DisplayName("should return false if email is null for user existence")
    void itShouldReturnFalseIfEmailIsNull(){
        String nullemail = null;

        // when
        Integer emptyemail = underTest.isUserExist(nullemail);

        // then
        assertThat(emptyemail).isEqualTo(0);
    }

    @Test
    @DisplayName("should return false if email is wrong for user existence")
    void itShouldReturnFalseIfEmailIsWrong(){
        String wrongemail = "wrong";
        // when
        Integer notExists = underTest.isUserExist(wrongemail);

        // then
        assertThat(notExists).isEqualTo(0);
    }

    @Test
    @DisplayName("should add moh admin")
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
    }

    @Test
    @DisplayName("should add hospital admin")
    void itShouldAddHospitalAdmin() {
        User user = new User(
                null,
                "pwdpwd",
                "mohadmn_usr@gmail.com",
                "f_name",
                "l_name",
                "HOSPITAL_ADMIN",
                "999999999v",
                3
        );

        Optional<User> output = underTest.addUser(
                user.getEmail(),user.getRole(),user.getNic(),user.getPassword(),user.getFirst_name(),user.getLast_name(),user.getHospital_id()
        );

        User expectedUser = output.get();
        user.setUser_id(expectedUser.getUser_id());
        user.setHospital_id(0);
        assertThat(expectedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("should add hospital user")
    void itShouldAddHospitalUser() {
        User user = new User(
                null,
                "pwdpwd",
                "hospital_usr@gmail.com",
                "f_name",
                "l_name",
                "HOSPITAL_USER",
                "999999999v",
                3
        );

        Optional<User> output = underTest.addUser(
                user.getEmail(),user.getRole(),user.getNic(),user.getPassword(),user.getFirst_name(),user.getLast_name(),user.getHospital_id()
        );

        User expectedUser = output.get();
        user.setUser_id(expectedUser.getUser_id());
        user.setHospital_id(0);
        assertThat(expectedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("should add moh user")
    void itShouldAddMOHUser() {
        User user = new User(
                null,
                "pwdpwd",
                "mohadmn_usr@gmail.com",
                "f_name",
                "l_name",
                "MOH_USER",
                "999999999v",
                0
        );

        Optional<User> output = underTest.addUser(
                user.getEmail(),user.getRole(),user.getNic(),user.getPassword(),user.getFirst_name(),user.getLast_name(),user.getHospital_id()
          );

        User expectedUser = output.get();
        user.setUser_id(expectedUser.getUser_id());
        assertThat(expectedUser).isEqualTo(user);
    }


    @Test
    @DisplayName("should raise exception if user role is invalid")
    void itShouldRaiseExceptionWhenAddUserWithInvalidUserRole() {
        User user = new User(
                null,
                "pwdpwd",
                "mohadmn_usr@gmail.com",
                "f_name",
                "l_name",
                "MOH_",
                "999999999v",
                0
        );

        assertThatExceptionOfType(JpaSystemException.class).isThrownBy(() -> {
            underTest.addUser(
                    user.getEmail(),user.getRole(),user.getNic(),user.getPassword(),user.getFirst_name(),user.getLast_name(),user.getHospital_id()
            );
        }).withMessage("could not extract ResultSet; " +
                "nested exception is org.hibernate.exception.GenericJDBCException: " +
                "could not extract ResultSet");
    }
}