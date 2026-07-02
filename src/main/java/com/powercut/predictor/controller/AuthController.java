package com.powercut.predictor.controller;

import com.powercut.predictor.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestParam String email,
            @RequestParam String password) {

        // Simple check for demo
        // In production connect to UserRepository
        if (email != null && password != null
                && password.length() >= 4) {
            String token =
                    jwtService.generateToken(email);
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "email", email,
                    "message", "Login successful"
            ));
        }
        return ResponseEntity
                .status(401)
                .body(Map.of("error",
                        "Invalid credentials"));
    }
}
