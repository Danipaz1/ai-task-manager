package com.pazdev.backend.service;

import java.util.List;

import com.pazdev.backend.dto.taskDTO.CreateTaskDTO;
import com.pazdev.backend.dto.taskDTO.TaskResponseDTO;
import com.pazdev.backend.dto.taskDTO.UpdateTaskDTO;

public interface TaskService {

    TaskResponseDTO getTaskById(Long taskId);

    List<TaskResponseDTO> getTasksByUserId(Long userId);

    List<TaskResponseDTO> getTasksByParams(String title, String status);

    TaskResponseDTO createTask(CreateTaskDTO createTaskDTO);

    TaskResponseDTO updateTask(Long taskId, UpdateTaskDTO updateTaskDTO);

    void deleteTask(Long taskId);
}