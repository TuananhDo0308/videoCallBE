package com.example.demo.Controller;


import com.example.demo.Model.LoginResponse;
import com.example.demo.Model.Role;
import com.example.demo.Model.RoomChat;
import com.example.demo.Model.User;
import com.example.demo.DTO.LoginDto;
import com.example.demo.DTO.SignUpDto;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.RoomChatRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Security.JwtIssuer;
import com.example.demo.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor()
public class AuthController {

    private final AuthService authService;
    private  final UserRepository userRepository;
    private final RoomChatRepository roomChatRepository;
    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginDto loginDto){
       return authService.signIn(loginDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignUpDto signUpDto){
        return authService.signUp(signUpDto);
    }
    @GetMapping("/room")
    public ResponseEntity<List<RoomChat>> getRooms(){
        return ResponseEntity.ok(roomChatRepository.findAll());
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}