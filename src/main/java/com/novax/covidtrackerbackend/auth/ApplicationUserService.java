package com.novax.covidtrackerbackend.auth;

import com.novax.covidtrackerbackend.model.User;
import com.novax.covidtrackerbackend.security.ApplicationUserRole;
import com.novax.covidtrackerbackend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService{

    
    private final UserService userService;

    @Autowired
	public ApplicationUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userService
                        .getUserByEmail(email)
                        .orElseThrow(() -> 
                                new UsernameNotFoundException(String.format("User %s not found", email)));
                                
        ApplicationUserRole role = ApplicationUserRole.valueOf(user.getRole());

        String uname = user.getEmail();
        String pwd = user.getPassword();
        String str_role = user.getRole();
        Long id = user.getUser_id();

        ApplicationUser applicationUser = new ApplicationUser(
            role.getGrantedAuthority(),
            pwd,
            uname,
                id,
                str_role,
                true,
            true,
            true,
            true
        );
        
		return applicationUser;
	}
    
}
