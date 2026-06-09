package com.optguard.controller;

import com.optguard.dto.ApiDtos.AuthResponse;
import com.optguard.dto.ApiDtos.LoginRequest;
import com.optguard.dto.ApiDtos.RegisterRequest;
import com.optguard.dto.ApiDtos.UserResponse;
import com.optguard.dto.ApiMappers;
import com.optguard.entity.AppUser;
import com.optguard.exception.BadRequestException;
import com.optguard.repository.UserRepository;
import com.optguard.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserDetailsService userDetailsService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (request.password().length() < 8) {
            throw new BadRequestException("Password must be at least 8 characters");
        }
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email is already registered");
        }

        AppUser user = new AppUser();
        user.setEmail(email);
        user.setFullName(request.fullName().trim());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        AppUser saved = userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(saved.getEmail());
        return new AuthResponse(jwtService.generateToken(userDetails), ApiMappers.toUser(saved));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        String email = normalizeEmail(request.email());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.password()));
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        return new AuthResponse(jwtService.generateToken(userDetails), ApiMappers.toUser(user));
    }

    @GetMapping("/me")
    public UserResponse me(org.springframework.security.core.Authentication authentication) {
        AppUser user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new BadRequestException("Authenticated user not found"));
        return ApiMappers.toUser(user);
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
