package com.novax.covidtrackerbackend.jwt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.novax.covidtrackerbackend.auth.ApplicationUser;
import com.novax.covidtrackerbackend.jwt.authexceptionhandlers.CustomAuthenticationFailureHandler;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

// verifying credentials
public class JwtAuthenticationAndPasswordFilter extends UsernamePasswordAuthenticationFilter{

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey jwtSecretKey;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    public JwtAuthenticationAndPasswordFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig,
                                              SecretKey jwtSecretKey, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.jwtSecretKey = jwtSecretKey;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    // This method calls when first time login fails
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        this.logger.trace("Failed to process authentication request", failed);
        this.logger.trace("Cleared SecurityContextHolder");
        this.logger.trace("Handling authentication failure");
        this.customAuthenticationFailureHandler.onAuthenticationFailure(request, response, failed);
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        ApplicationUser userDetails = ((ApplicationUser) authResult.getPrincipal()); // get the principal (logged-in user) object

        // TODO : need to encode userdetails into jwt and send
        // create JWT
                String token = Jwts.builder()
                        .setSubject(authResult.getName())
                        .claim("authorities",authResult.getAuthorities())
                        .claim("id",userDetails.getId())
                        .setIssuedAt(new Date())
                        .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(4)))
                        .signWith(jwtSecretKey)
                        .compact();

        // create custom authentication success response
        CustomAuthenticationSuccessResponse customAuthenticationSuccessResponse
                = new CustomAuthenticationSuccessResponse();
        String resBody = customAuthenticationSuccessResponse.getResponse(response,userDetails,request);

        // sending JWT in the hdr
        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
        // adding body to response
        response.getWriter().write(resBody);
    }
    

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            // extract email and password
            UsernameAndPasswordAuthenticationRequest authRequest = new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);
            // create authentication
            Authentication auth = new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword());

            return authenticationManager.authenticate(auth);
            
        } catch (JsonParseException e) {
            e.printStackTrace();

        } catch (JsonMappingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return super.attemptAuthentication(request, response);
    }
    
}
