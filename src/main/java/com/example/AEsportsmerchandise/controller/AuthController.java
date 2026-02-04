package com.example.AEsportsmerchandise.controller;

import com.example.AEsportsmerchandise.dto.AuthRequest;
import com.example.AEsportsmerchandise.dto.AuthResponse;
import com.example.AEsportsmerchandise.entity.UserEntity;
import com.example.AEsportsmerchandise.repository.UserRepository;
import com.example.AEsportsmerchandise.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Authentication and authorization APIs for users and admins"
)
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with USER role"
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody AuthRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already exists"
            );
        }

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
    }

    @Operation(
            summary = "Register a new admin",
            description = "Creates a new admin account with ADMIN role"
    )
    @PostMapping("/admin/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAdmin(@RequestBody AuthRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already exists"
            );
        }

        UserEntity admin = new UserEntity();
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole("ADMIN");

        userRepository.save(admin);
    }

    @Operation(
            summary = "Login",
            description = "Authenticates user credentials and returns a JWT token"
    )
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "User not found"
                        )
                );

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid credentials"
            );
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole()
        );

        return new AuthResponse(token);
    }
}
