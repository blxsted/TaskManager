package com.example.todoApp.task.service;

import com.example.todoApp.task.model.TaskPriority;
import com.example.todoApp.task.model.TaskStatus;
import com.example.todoApp.task.model.dto.TaskCreateRequest;
import com.example.todoApp.task.model.dto.TaskResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {

    TaskResponse createTask(TaskCreateRequest task);

    TaskResponse updateTask(TaskCreateRequest task, Long id);

    void deleteTask(Long taskId);

    List<TaskResponse> getTasks(TaskStatus status, TaskPriority priority, LocalDateTime dueDate);

    TaskResponse getTaskById(Long id);
}
