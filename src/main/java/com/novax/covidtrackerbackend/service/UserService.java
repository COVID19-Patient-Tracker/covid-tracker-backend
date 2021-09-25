package com.novax.covidtrackerbackend.service;

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
import org.springframework.stereotype.Service;

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

    public List<User> getAllUsers(){
        return (List<User>) userRepository.findAll();
    }

    public boolean isUserExist(String email) {
        return userRepository.isUserExist(email);
    }

    public Optional<User> getUserByEmail(String email){
        Optional<User> user = userRepository.getUserByEmail(email);
        return user;
    }

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
        if (created_user != null){
            try{
                sendFirstLoginEmail(user.getEmail());
            } catch (IOException e) {
                throw new InvalidOperationException("Operation Failed!");
            }
        }
        return created_user;
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
