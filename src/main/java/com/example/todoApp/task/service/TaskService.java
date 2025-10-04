package com.example.todoApp.task.service;

import com.example.todoApp.task.model.dto.TaskCreateRequest;
import com.example.todoApp.task.model.dto.TaskResponse;

public interface TaskService {


    TaskResponse createTask(TaskCreateRequest task);

    void updateTask(TaskCreateRequest task);

    void deleteTask();

}
