package com.novax.covidtrackerbackend.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.novax.covidtrackerbackend.auth.ApplicationUserService;
import com.novax.covidtrackerbackend.jwt.JwtAuthenticationAndPasswordFilter;
import com.novax.covidtrackerbackend.jwt.JwtConfig;
import com.novax.covidtrackerbackend.jwt.JwtTokenAuthentication;
import com.novax.covidtrackerbackend.jwt.authexceptionhandlers.CustomAuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig  extends WebSecurityConfigurerAdapter{

    private final PasswordEncoder passwordEncoder;
    private final com.novax.covidtrackerbackend.auth.ApplicationUserService userDetailsService;
    private final CustomAuthenticationFailureHandler simpleAuthenticationFailureHandler;
    private final SecretKey jwtSecretKey;
    private final JwtConfig jwtConfig;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder pwencdr,
                                     ApplicationUserService userDetailsService, SecretKey jwtSecretKey,
                                     JwtConfig jwtConfig, CustomAuthenticationFailureHandler simpleAuthenticationFailureHandler){
        this.passwordEncoder = pwencdr;
        this.userDetailsService = userDetailsService;
        this.simpleAuthenticationFailureHandler = simpleAuthenticationFailureHandler;
        this.jwtSecretKey = jwtSecretKey;
        this.jwtConfig = jwtConfig;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login","index","/css/*","/js/*","/index","/app/V1/user")
                .permitAll().and()
                .addFilter(new JwtAuthenticationAndPasswordFilter(authenticationManager(), jwtConfig, jwtSecretKey, new CustomAuthenticationFailureHandler())).exceptionHandling().and()
                .addFilterAfter(new JwtTokenAuthentication(jwtSecretKey, jwtConfig, simpleAuthenticationFailureHandler),JwtAuthenticationAndPasswordFilter.class).exceptionHandling();

                
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService(){

        return super.userDetailsService();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth){

        auth.authenticationProvider(daoAuthenticationProvider());

    }

    @Bean
	protected DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        provider.setHideUserNotFoundExceptions(false);
		return provider;

	}

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").exposedHeaders("*");
            }
        };
    }

}
