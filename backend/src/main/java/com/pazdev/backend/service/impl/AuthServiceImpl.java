package com.pazdev.backend.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pazdev.backend.dto.authDTO.AuthResponseDTO;
import com.pazdev.backend.dto.authDTO.LoginRequestDTO;
import com.pazdev.backend.dto.authDTO.RegisterRequestDTO;
import com.pazdev.backend.entity.User;
import com.pazdev.backend.exception.BadRequestException;
import com.pazdev.backend.repository.UserRepository;
import com.pazdev.backend.security.JwtService;
import com.pazdev.backend.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            UserRepository userRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        // Implementation for user registration
        String name = request.getName();
        String email = request.getEmail();

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already in use");

        }

        String password = request.getPassword();
        password = passwordEncoder.encode(password);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);

        return new AuthResponseDTO(jwtService.generateToken(email));
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        // Implementation for user login
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        String token = jwtService.generateToken(request.getEmail());

        return new AuthResponseDTO(token);
    }
}