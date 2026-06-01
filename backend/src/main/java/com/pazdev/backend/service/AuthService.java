package com.pazdev.backend.service;

import com.pazdev.backend.dto.authDTO.AuthResponseDTO;
import com.pazdev.backend.dto.authDTO.LoginRequestDTO;
import com.pazdev.backend.dto.authDTO.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO request);
    AuthResponseDTO login(LoginRequestDTO request);
}