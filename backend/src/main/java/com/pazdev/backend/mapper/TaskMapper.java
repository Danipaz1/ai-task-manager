package com.pazdev.backend.mapper;

import org.springframework.stereotype.Component;

import com.pazdev.backend.dto.taskDTO.TaskResponseDTO;
import com.pazdev.backend.entity.Task;

@Component
public class TaskMapper {

    public TaskResponseDTO toResponse(Task task) {

        return new TaskResponseDTO(
            task.getId(),
            task.getTitle(),
            task.getStatus()
        );
    }
}
