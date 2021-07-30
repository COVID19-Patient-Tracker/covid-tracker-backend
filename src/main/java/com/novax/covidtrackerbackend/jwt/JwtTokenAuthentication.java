package com.novax.covidtrackerbackend.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import java.util.stream.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class JwtTokenAuthentication extends OncePerRequestFilter{

    private final SecretKey jwtSecretKey;
    private final JwtConfig jwtConfig;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHdr = request.getHeader(jwtConfig.getAuthorizationHeader());

        if(Strings.isNullOrEmpty(authorizationHdr) || !authorizationHdr.startsWith("Bearer")){

            filterChain.doFilter(request, response);
            return;

        }

        String token = authorizationHdr.replace(jwtConfig.getTokenprefix(), "");
        
        try {
            

            Jws<Claims> claimJws = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token);

                    Claims Body = claimJws.getBody();

                    String usrname = Body.getSubject();

                    var authorities = (List<Map<String, String>>) Body.get("authorities");

                    Set<SimpleGrantedAuthority> 
                        simpleGrantedAuthorities = 
                            authorities.stream()
                                            .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                                            .collect(Collectors.toSet());

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                        usrname, 
                        null,
                        simpleGrantedAuthorities
                        );

                    SecurityContextHolder.getContext()
                                            .setAuthentication(authentication);

        } catch (Exception e) {

            throw new IllegalStateException(String.format("Token %s not valid", token));

        }

        filterChain.doFilter(request, response);
    }

    public JwtTokenAuthentication(SecretKey jwtSecretKey, JwtConfig jwtConfig) {
        this.jwtSecretKey = jwtSecretKey;
        this.jwtConfig = jwtConfig;
    }
    
}
