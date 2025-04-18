package com.example.demo.Service;

import com.example.demo.DTO.LoginDto;
import com.example.demo.DTO.SignUpDto;
import com.example.demo.Model.LoginResponse;
import com.example.demo.Model.Role;
import com.example.demo.Model.User;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Security.JwtIssuer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtIssuer jwtIssuer;
    private final UserService userService;


    public ResponseEntity<LoginResponse> signIn(LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByUsernameOrEmail(userDetails.getUsername(), userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Set<Role>roles = userService.getRolesForUser(user.getUsername());
        var token = jwtIssuer.issue(user.getId(),user.getEmail(),user.getUsername(),user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        return new ResponseEntity<>(
                LoginResponse.builder()
                        .token(token)
                        .username(user.getName())
                        .build(),
                HttpStatus.OK);
    }

    public ResponseEntity<String> signUp(SignUpDto signUpDto){
        // add check for email exists in DB
        if(userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        // create user object
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getEmail());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        Role roles;
        if ( !signUpDto.getType().isEmpty() && signUpDto.getType().equals("Admin")) {
            roles = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Admin role not found!"));
        } else {
            roles = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("User role not found!"));
        }
        user.setRoles(new HashSet<>(Collections.singleton(roles)));
        userRepository.save(user);

        return new ResponseEntity<>("Succes with role :" + roles.getName(), HttpStatus.OK);
    }
}
