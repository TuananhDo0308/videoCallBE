package com.example.demo.Security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtToPrincipalConverter {
    public UserPrincipal convert(DecodedJWT jwt) {

        return UserPrincipal.builder()
                .id(Long.valueOf(jwt.getSubject())) // Assuming "sub" is also the user ID
                .email(jwt.getClaim("email").asString())
                .username(jwt.getClaim("username").asString()) // Set the username
                .email(jwt.getClaim("email").asString())
                .authorities(extractAuthorities(jwt))
                .build();
    }

    private List<SimpleGrantedAuthority> extractAuthorities(DecodedJWT decodedJWT) {
        var claim = decodedJWT.getClaim("authorities");
        if (claim.isNull() || claim.isMissing()) {
            return List.of();
        }
        return claim.asList(String.class)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}