package com.novax.covidtrackerbackend.jwt;

import javax.crypto.SecretKey;

import com.google.common.net.HttpHeaders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;

@ConfigurationProperties(prefix = "application.jwt")
@Configuration
@NoArgsConstructor
@Getter
@Setter
public class JwtConfig {

    private String secret;
    private String tokenprefix;
    private Integer tokenExpirationAfterWeeks;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTokenprefix() {
        return tokenprefix;
    }

    public void setTokenprefix(String tokenprefix) {
        this.tokenprefix = tokenprefix;
    }

    public Integer getTokenexpirationafterweeks() {
        return tokenExpirationAfterWeeks;
    }

    public void setTokenexpirationafterweeks(Integer tokenexpirationafterweeks) {
        this.tokenExpirationAfterWeeks = tokenexpirationafterweeks;
    }

    public SecretKey getSecretKeyForSigning(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String getAuthorizationHeader(){
        return HttpHeaders.AUTHORIZATION;
    }
    
}
