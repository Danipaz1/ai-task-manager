package com.pazdev.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pazdev.backend.dto.taskDTO.CreateTaskDTO;
import com.pazdev.backend.dto.taskDTO.TaskResponseDTO;
import com.pazdev.backend.dto.taskDTO.UpdateTaskDTO;
import com.pazdev.backend.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/{taskId}")
    public TaskResponseDTO getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId);
    }

    @GetMapping("/user/{userId}")
    public List<TaskResponseDTO> getTasksByUserId(@PathVariable Long userId) {
        return taskService.getTasksByUserId(userId);
    }

    @GetMapping
    public List<TaskResponseDTO> getTasksByParams(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status) {
        return taskService.getTasksByParams(title, status);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody CreateTaskDTO createTaskDTO) {
        TaskResponseDTO response = taskService.createTask(createTaskDTO);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    @PatchMapping("/{taskId}")
    public TaskResponseDTO updateTask(@PathVariable Long taskId, @RequestBody UpdateTaskDTO updateTaskDTO) {
        return taskService.updateTask(taskId, updateTaskDTO);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }
}