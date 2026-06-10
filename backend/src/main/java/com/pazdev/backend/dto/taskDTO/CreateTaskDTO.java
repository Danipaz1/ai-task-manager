package com.pazdev.backend.dto.taskDTO;

import jakarta.validation.constraints.NotBlank;

public class CreateTaskDTO {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Status cannot be blank")
    private String status;

    @NotBlank(message = "User ID cannot be blank")
    private Long userId;

    public CreateTaskDTO() {}

    public CreateTaskDTO(String title, String description, String status, Long userId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.userId = userId;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
