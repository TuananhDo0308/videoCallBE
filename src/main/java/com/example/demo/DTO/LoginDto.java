package com.example.demo.DTO;


import lombok.Data;

@Data
public class LoginDto {
    private String usernameOrEmail;
    private String password;
}