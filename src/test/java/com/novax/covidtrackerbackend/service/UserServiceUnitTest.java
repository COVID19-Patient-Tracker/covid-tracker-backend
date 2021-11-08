package com.novax.covidtrackerbackend.service;
import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
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
    void isUserExist() {
        // when
        Integer isUserExist = underTest.isUserExist("abc@gmail.com");
        // then
        verify(userRepository).isUserExist("abc@gmail.com");
    }

    @Test
    void isAbleToGetUserById() {
        // when
        try {
            Optional<User> user = underTest.getUserById(100477L);
        } catch (SQLException ignored) {
        }
        // then
        verify(userRepository).findById(100477L);
    }

    @Test
    void isAbleToGetUserByEmail() {
        // when
        Optional<User> user = underTest.getUserByEmail("abc@gmail.com");
        // then
        verify(userRepository).getUserByEmail("abc@gmail.com");
    }

    @Test
    void isAbleToDeleteUser() {
        // when
        underTest.deleteUser(9999L);
        // then
        verify(userRepository).deleteById(9999L);
    }

    @Test
    void isAbleToSave() {
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
        // when
        User u = underTest.save(user);
        // then
        verify(userRepository).save(user);
    }

    @Test
    void isAbleToAddUser() {
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
        // when
        Optional<User> u = underTest.addUser(user);
        // then
        verify(userRepository).addUser(
                user.getEmail(),
                user.getRole(),user.getNic(),user.getPassword(),user.getFirst_name(),
                user.getLast_name(),user.getHospital_id());
    }

    @Test
    void updateUserPassword() throws SQLException {
        User user = new User(
                9999L,
                "$2a$10$uHTQrwQWUSAgWMekvYgqduJ2odI9gdGpdFu7zXR5816/drVQfRroC",
                "mohadmn@gmail.com",
                "f_name",
                "l_name",
                "MOH_ADMIN",
                "999999999v",
                0
        );

        User newuser = new User(
                null,
                9999L,
                "password",
                "mohadmn@gmail.com",
                "f_name",
                "l_name",
                "MOH_ADMIN",
                "999999999v",
                0,
                "new_password"
        );


        Authentication auth = new Authentication() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return anyLong();
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
        };

//        given(userRepository.findById(anyLong()))
//                .willReturn(Optional.of(user));

//        User updatedUser = underTest.updateUserPassword(newuser,auth);

//        verify(userRepository).findById(0L);
//        verify(userRepository).save(any());
    }

    @Test
    void updateUserDetails() throws SQLException {
        User user = new User(
                9999L,
                "password",
                "mohadmn@gmail.com",
                "f_name",
                "l_name",
                "MOH_ADMIN",
                "999999999v",
                0
        );


        Authentication auth = new Authentication() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return 100477L;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
        };

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        user.setUser_id(99999L);
        User updatedUser = underTest.updateUserDetails(user,auth);

        verify(userRepository).findById(anyLong());
        verify(userRepository).save(user);
    }

    @Test
    void ShouldUpdateUserDetailsFail(){
        User user = new User(
                9999L,
                "password",
                "mohadmn@gmail.com",
                "f_name",
                "l_name",
                "MOH_ADMIN",
                "999999999v",
                0
        );

        User newDetailsOfuser = new User(
                999999999L,
                "pw",
                "new@gmail.com",
                "_new_f_name",
                "_new_l_name",
                "MOH_ADMIN",
                "999999999v",
                0
        );

        assertThatThrownBy(() -> {
            Authentication auth = new Authentication() {
                @Override
                public String getName() {
                    return null;
                }

                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return null;
                }

                @Override
                public Object getCredentials() {
                    return 100477L;
                }

                @Override
                public Object getDetails() {
                    return null;
                }

                @Override
                public Object getPrincipal() {
                    return null;
                }

                @Override
                public boolean isAuthenticated() {
                    return false;
                }

                @Override
                public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

                }
            };

            given(userRepository.findById(anyLong()))
                    .willReturn(Optional.of(user));

            User updatedUser = underTest.updateUserDetails(newDetailsOfuser,auth);
        })
                .isInstanceOf(SQLException.class)
                .hasMessage("id in database and provided id doesn't match");

        verify(userRepository).findById(anyLong());
        verify(userRepository,never()).save(any());
    }

    @Test
    void sendEmail() {

    }

    @Test
    void sendFirstLoginEmail() {

    }

    // see about argument captor
}
