package com.pazdev.backend.mapper;

import org.springframework.stereotype.Component;

import com.pazdev.backend.dto.userDTO.UserResponseDTO;
import com.pazdev.backend.entity.User;

@Component
public class UserMapper {
    
    public UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail()
        );
    }
}
