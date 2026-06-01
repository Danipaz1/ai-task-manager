package com.pazdev.backend.service;

import java.util.List;

import com.pazdev.backend.dto.userDTO.UserResponseDTO;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
}