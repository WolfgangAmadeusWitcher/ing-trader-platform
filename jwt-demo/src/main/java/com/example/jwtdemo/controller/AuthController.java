package com.example.jwtdemo.controller;

import com.example.jwtdemo.dto.LoginRequest;
import com.example.jwtdemo.dto.LoginResponse;
import com.example.jwtdemo.dto.SignUpRequest;
import com.example.jwtdemo.dto.SignUpResponse;
import com.example.jwtdemo.model.UserRole;
import com.example.jwtdemo.security.JwtUtil;
import com.example.jwtdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpirationTime;

    @PostMapping("/register")
    public ResponseEntity<SignUpResponse> registerUser(@RequestBody SignUpRequest signUpRequest) {
        String username = signUpRequest.getUsername();
        String password = signUpRequest.getPassword();
        UserRole userRole = UserRole.STANDARD;
        userService.registerUser(username, password, userRole);
        return ResponseEntity.ok(new SignUpResponse("User Successfully Registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        String token = jwtUtil.generateToken(username);
        return ResponseEntity.ok(new LoginResponse(token, "Login Successful", jwtExpirationTime));
    }
}
