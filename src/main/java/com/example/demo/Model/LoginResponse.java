package com.example.demo.Model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private final String token;
    private final String username;
}
