package com.pazdev.backend.service;

import com.pazdev.backend.dto.AuthResponseDTO;
import com.pazdev.backend.dto.LoginRequestDTO;
import com.pazdev.backend.dto.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO request);
    AuthResponseDTO login(LoginRequestDTO request);
}