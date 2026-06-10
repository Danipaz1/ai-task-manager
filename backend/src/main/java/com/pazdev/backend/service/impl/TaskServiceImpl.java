package com.pazdev.backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pazdev.backend.dto.taskDTO.CreateTaskDTO;
import com.pazdev.backend.dto.taskDTO.TaskResponseDTO;
import com.pazdev.backend.dto.taskDTO.UpdateTaskDTO;
import com.pazdev.backend.entity.Task;
import com.pazdev.backend.entity.User;
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
            TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    private List<TaskResponseDTO> getAllTasks() {

        return mapTasks(taskRepository.findAll());
    }

    @Override
    public TaskResponseDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException(
                "Task not found with id: " + taskId));

        return taskMapper.toResponse(task);
    }

    @Override
    public List<TaskResponseDTO> getTasksByUserId(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return mapTasks(taskRepository.findByUserId(userId));
    }

    @Override
    public List<TaskResponseDTO> getTasksByParams(String title, String status) {

        boolean hasTitle = title != null && !title.isBlank();
        boolean hasStatus = status != null && !status.isBlank();

        if (hasTitle && hasStatus) {
            return mapTasks(taskRepository.findByTitleContainingAndStatus(title, status));
        } else if (hasTitle) {
            return mapTasks(taskRepository.findByTitleContaining(title));
        } else if (hasStatus) {
            return taskRepository.findByStatus(status).stream()
                    .map(taskMapper::toResponse)
                    .toList();
        } else {
            return getAllTasks();
        }
    }

    @Override
    public TaskResponseDTO createTask(CreateTaskDTO createTaskDTO) {
        User user = userRepository.findById(createTaskDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + createTaskDTO.getUserId()));

        Task task = new Task();
        task.setTitle(createTaskDTO.getTitle());
        task.setDescription(createTaskDTO.getDescription());
        task.setStatus(createTaskDTO.getStatus());
        task.setUser(user);
        task.setCreatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Override
    public TaskResponseDTO updateTask(Long taskId, UpdateTaskDTO updateTaskDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        if (updateTaskDTO.getTitle() != null && !updateTaskDTO.getTitle().isBlank()) {
            task.setTitle(updateTaskDTO.getTitle());
        }
        if (updateTaskDTO.getDescription() != null && !updateTaskDTO.getDescription().isBlank()) {
            task.setDescription(updateTaskDTO.getDescription());
        }
        if (updateTaskDTO.getStatus() != null && !updateTaskDTO.getStatus().isBlank()) {
            task.setStatus(updateTaskDTO.getStatus());
        }

        Task updatedTask = taskRepository.save(task);
        return taskMapper.toResponse(updatedTask);
    }

    private List<TaskResponseDTO> mapTasks(List<Task> tasks) {
        return tasks.stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        taskRepository.deleteById(taskId);
    }
}