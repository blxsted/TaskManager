package com.example.todoApp.task.service;

import com.example.todoApp.task.model.dto.TaskCreateRequest;
import com.example.todoApp.task.model.dto.TaskResponse;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(TaskCreateRequest task);

    List<TaskResponse> getAllTasks();

    TaskResponse updateTask(TaskCreateRequest task, Long id);

    void deleteTask(Long taskId);

}
