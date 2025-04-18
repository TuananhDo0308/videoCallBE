package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Áp dụng cho tất cả các endpoint
                        .allowedOrigins("https://f71a-171-252-189-105.ngrok-free.app") // Chỉ định origin cụ thể
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Cho phép các phương thức HTTP
                        .allowedHeaders("*") // Cho phép tất cả header
                        .allowCredentials(true); // Cho phép gửi credentials
            }
        };
    }
}