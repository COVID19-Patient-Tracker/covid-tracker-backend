package com.novax.covidtrackerbackend.service;

import java.sql.SQLException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.novax.covidtrackerbackend.exceptions.InvalidOperationException;
import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    private SendGridEmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${ADMIN_EMAIL_ADDRESS}")
    private String adminEmailAddress;

    @Value("${EMAIL_SIGNUP_TEMPLATE_ID}")
    private String Signup_tid;

//    @Autowired
    private PasswordEncoder passwordEncoder =  new BCryptPasswordEncoder(10);

    public List<User> getAllUsers(){
        return (List<User>) userRepository.findAll();
    }

    public Integer isUserExist(String email) {
        return userRepository.isUserExist(email);
    }

    public Optional<User> getUserByEmail(String email){
        Optional<User> user = userRepository.getUserByEmail(email);
        return user;
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public Optional<User> getUserById(Long id) throws SQLException {
        Optional<User> u = userRepository.findById(id);
        if(u.isEmpty()){
            throw new SQLException("requested data doesn't exists in database");
        }
        return u;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     *
     * @param user - user object to add to the database
     * @return new user details
     */
    public Optional<User> addUser(User user){
        Optional<User> created_user = userRepository.addUser(
                user.getEmail(),
                user.getRole(),
                user.getNic(),
                user.getPassword(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getHospital_id()
        );
        if (created_user.isPresent()){
            try{
                sendFirstLoginEmail(user.getEmail());
            } catch (IOException e) {
                throw new InvalidOperationException("Operation Failed!");
            }
        }
        return created_user;
    }
    /*** under construction
     *
     * @param userWithNewPassword - new details of the user
     * @param auth - authentication object in the context - to verify user updating their own details by comparing id
     * @return user
     * @throws - SQLException
     */
    public synchronized User updateUserPassword(User userWithNewPassword, Authentication auth) throws SQLException {

        Long u_id = userWithNewPassword.getUser_id();

        // user doesn't exist exception is handled
        // get previous info by id
        Optional<User> previousDetailsOfUser = this.getUserById(Long.valueOf(String.valueOf(auth.getCredentials())));
        User updatedDetailsOfUser = null;

        if(previousDetailsOfUser.isPresent()){
            // get old password for verify process
            User user = previousDetailsOfUser.get();
            String previousEncodedPassword = user.getPassword();

            // get old password send by client to change
            String password = userWithNewPassword.getPassword();
            boolean isPasswordMatched = passwordEncoder.matches(password,previousEncodedPassword);

            userWithNewPassword.setRole(previousDetailsOfUser.get().getRole());

            if(isPasswordMatched) {

                // encode password
                String encodedNewPassword = passwordEncoder.encode(userWithNewPassword.getNew_password());
                // set encoded password
                userWithNewPassword.setPassword(encodedNewPassword);
                // save new password with user
                updatedDetailsOfUser = this.save(userWithNewPassword);

            }else{
                throw new SQLException("id/password mismatch"); // this exception throws to handle
            }
        }

        // always return non-null object
        return updatedDetailsOfUser;
    }

    /***
     *
     * @param newDetailsOfUser - new details of the user
     * @param auth - authentication object in the context - to verify user updating their own details by comparing id
     * @return
     * @throws SQLException
     */
    public synchronized User updateUserDetails(User newDetailsOfUser, Authentication auth) throws SQLException {

        Long u_id = newDetailsOfUser.getUser_id();

        // user doesn't exist exception is handled
        // get previous info by id
        Optional<User> previousDetailsOfUser = this.getUserById(Long.valueOf(String.valueOf(auth.getCredentials())));
        User updatedDetailsOfUser = null;

        if(previousDetailsOfUser.isPresent()){
            // password and role changes not permitted here (so rest of them if they have changed by malformed activity or any AFJ)
            newDetailsOfUser.setPassword(previousDetailsOfUser.get().getPassword());
            newDetailsOfUser.setRole(previousDetailsOfUser.get().getRole());

            if(previousDetailsOfUser.get().getUser_id().equals(newDetailsOfUser.getUser_id())) {
                updatedDetailsOfUser = this.save(newDetailsOfUser);
            }else{
                throw new SQLException("id in database and provided id doesn't match"); // this exception throws to handle
            }
        }
        // always return non-null object
        return updatedDetailsOfUser;
    }

    /**
     * Email Sending service
     */
    public void sendEmail(String to, String template_id, Map<String, String> dynamic_data) throws IOException {
        emailService.sendHTMLEmail(
                adminEmailAddress,
                to,
                template_id,
                dynamic_data
        );
    }
    public void sendFirstLoginEmail(String userEmail) throws IOException {
        Map<String, String> dynamic_data = new HashMap<String, String>();
        sendEmail(
                userEmail,
                Signup_tid,
                dynamic_data
        );
    }


}
