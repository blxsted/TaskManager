package com.example.todoApp.task.model.dto;

import com.example.todoApp.task.model.TaskPriority;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskCreateRequest {

    private String title;
    private String description;
    private TaskPriority priority;
    private LocalDateTime dueDate;

}
