package com.pazdev.backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pazdev.backend.dto.taskDTO.TaskResponseDTO;
import com.pazdev.backend.exception.ResourceNotFoundException;
import com.pazdev.backend.mapper.TaskMapper;
import com.pazdev.backend.repository.TaskRepository;
import com.pazdev.backend.repository.UserRepository;
import com.pazdev.backend.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(
        TaskRepository taskRepository,
        UserRepository userRepository,
        TaskMapper taskMapper){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<TaskResponseDTO> getAllTasks() {

        return taskRepository.findAll()
        .stream()
        .map(taskMapper::toResponse)
        .toList();
    }

    @Override
    public List<TaskResponseDTO> getTasksByUserId(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return taskRepository.findByUserId(userId).stream()
                .map(taskMapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<TaskResponseDTO> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status).stream()
                .map(taskMapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<TaskResponseDTO> getTasksBySearch(String title, String status) {

        boolean hasTitle = title != null && !title.isBlank();
        boolean hasStatus = status != null && !status.isBlank();

        if(hasTitle && hasStatus) {
            return taskRepository.findByTitleContainingAndStatus(title, status).stream()
                    .map(taskMapper::toResponse)
                    .collect(java.util.stream.Collectors.toList());
        } else if(hasTitle) {
            return taskRepository.findByTitleContaining(title).stream()
                    .map(taskMapper::toResponse)
                    .collect(java.util.stream.Collectors.toList());
        } else if(hasStatus) {
            return taskRepository.findByStatus(status).stream()
                    .map(taskMapper::toResponse)
                    .collect(java.util.stream.Collectors.toList());
        } else {
            return getAllTasks();
        }
    }
}