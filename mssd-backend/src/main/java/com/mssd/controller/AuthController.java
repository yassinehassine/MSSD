package com.mssd.controller;

import com.mssd.dto.LoginRequest;
import com.mssd.dto.LoginResponse;
import com.mssd.dto.UserDto;
import com.mssd.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = userService.authenticate(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(LoginResponse.builder()
                    .success(false)
                    .message("Invalid credentials")
                    .build());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        try {
            UserDto createdUser = userService.createUser(userDto);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        try {
            UserDto user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/hash-password")
    public ResponseEntity<Map<String, String>> hashPassword(@RequestBody Map<String, String> request) {
        try {
            String plainPassword = request.get("password");
            
            if (plainPassword == null || plainPassword.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Password cannot be empty");
                return ResponseEntity.badRequest().body(error);
            }
            
            String hashedPassword = passwordEncoder.encode(plainPassword);
            
            Map<String, String> response = new HashMap<>();
            response.put("hashedPassword", hashedPassword);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to hash password: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
