package com.example.demo.Security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("security.jwt")
public class JwtProperties {
    private String secretKey; }
