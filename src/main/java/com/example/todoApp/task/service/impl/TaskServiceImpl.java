package com.example.todoApp.task.service.impl;

import com.example.todoApp.task.model.TaskEntity;
import com.example.todoApp.task.model.TaskStatus;
import com.example.todoApp.task.model.dto.TaskCreateRequest;
import com.example.todoApp.task.model.dto.TaskResponse;
import com.example.todoApp.task.repository.TaskRepository;
import com.example.todoApp.task.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public TaskResponse createTask(TaskCreateRequest task){

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(task.getTitle());
        taskEntity.setDescription(task.getDescription());
        taskEntity.setStatus(TaskStatus.OPEN);
        taskEntity.setPriority(task.getPriority());
        taskEntity.setDueDate(task.getDueDate());

        TaskEntity saved = taskRepository.save(taskEntity);

        return new TaskResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getStatus(),
                saved.getPriority(),
                saved.getDueDate(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
                );

    }

    @Override
    @Transactional
    public void updateTask(TaskCreateRequest task){
    }

    @Override
    @Transactional
    public void deleteTask(){}

}
