package com.pazdev.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pazdev.backend.dto.taskDTO.TaskResponseDTO;
import com.pazdev.backend.exception.ResourceNotFoundException;
import com.pazdev.backend.repository.TaskRepository;
import com.pazdev.backend.repository.UserRepository;
import com.pazdev.backend.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(
        TaskRepository taskRepository,
        UserRepository userRepository){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> new TaskResponseDTO(task.getId(), task.getTitle(), task.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<TaskResponseDTO> getTasksByUserId(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return taskRepository.findByUserId(userId).stream()
                .map(task -> new TaskResponseDTO(task.getId(), task.getTitle(), task.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<TaskResponseDTO> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status).stream()
                .map(task -> new TaskResponseDTO(task.getId(), task.getTitle(), task.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<TaskResponseDTO> getTasksBySearch(String title) {
        return taskRepository.findByTitleContaining(title).stream()
                .map(task -> new TaskResponseDTO(task.getId(), task.getTitle(), task.getStatus()))
                .collect(java.util.stream.Collectors.toList());
    }
}