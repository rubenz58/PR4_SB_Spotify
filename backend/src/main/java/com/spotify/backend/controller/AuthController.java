package com.spotify.backend.controller;

import com.spotify.backend.dto.UserDTO;
import com.spotify.backend.service.JwtService;
import com.spotify.backend.service.UserService;
import com.spotify.backend.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, String> request) {
        try {
            User user = userService.createUser(
                    request.get("email"),
                    request.get("name"),
                    request.get("password")
            );

            String token = jwtService.generateToken(user);

            UserDTO userDTO = new UserDTO(user);

            Map<String, Object> response = Map.of(
                    "message", "User created successfully",
                    "token", token,
                    "user", userDTO
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            // 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));

        } catch (IllegalStateException e) {
            // 409 Conflict
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        // TODO: Implement login logic
        // 1. Check if email is in DB
        // 2. Check if password matches the hash.
        // 3. return in correct format.
        try {
            User user = userService.login(
                    request.get("email"),
                    request.get("password")
            );

            String token = jwtService.generateToken(user);

            UserDTO userDTO = new UserDTO(user);

            Map<String, Object> response = Map.of(
                    "message", "User logged in successfully",
                    "token", token,
                    "user", userDTO
            );

            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public Map<String, Object> logout() {
        // TODO: Implement logout logic
        return Map.of("message", "Logout endpoint");
    }

    @GetMapping("/me")
    public Map<String, Object> me() {
        // TODO: Implement get current user logic
        return Map.of("message", "Get current user endpoint");
    }
}
