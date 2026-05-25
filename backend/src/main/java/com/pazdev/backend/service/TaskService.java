package com.pazdev.backend.service;

import java.util.List;

import com.pazdev.backend.dto.TaskResponseDTO;

public interface TaskService {
    List<TaskResponseDTO> getAllTasks();

    List<TaskResponseDTO> getTasksByUserId(Long userId);

    List<TaskResponseDTO> getTasksByStatus(String status);

    List<TaskResponseDTO> getTasksBySearch(String title);
}