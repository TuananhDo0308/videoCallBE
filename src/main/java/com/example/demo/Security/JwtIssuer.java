package com.example.demo.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
public class JwtIssuer {
    @Autowired
    private  JwtProperties jwtProperties;

    public String issue(Long id, String username, String email, List<String> roles) {        String secret = jwtProperties.getSecretKey();
        return JWT.create()
                .withSubject(String.valueOf(id))
                .withClaim("email", email)
                .withClaim("username", username)
                .withClaim("authorities", roles)
                .withExpiresAt(Instant.now().plus(Duration.ofDays(1)))
                .sign(Algorithm.HMAC256(secret));
    }

}
