package com.novax.covidtrackerbackend.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;




@Configuration
public class JwtSecretKey {
    
    private final JwtConfig jwtConfig;

    @Autowired
    public JwtSecretKey(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Bean
    public SecretKey getSecret(){
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }
}
