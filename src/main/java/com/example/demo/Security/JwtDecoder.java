package com.example.demo.Security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtDecoder {
    private final JwtProperties jwtProperties;

    public DecodedJWT decode(String token) {
        try {
            String secret = jwtProperties.getSecretKey();
            return JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);
        } catch (SignatureVerificationException e) {
            System.err.println("Invalid signature for token: " + token);
            throw e;
        } catch (TokenExpiredException e) {
            System.err.println("Token expired: " + token);
            throw e;
        } catch (Exception e) {
            System.err.println("Error decoding token: " + e.getMessage());
            throw e;
        }
    }
}
