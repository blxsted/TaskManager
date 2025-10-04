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

import java.util.List;
import java.util.stream.Collectors;

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
    public List<TaskResponse> getAllTasks() {
        List<TaskEntity> tasks = taskRepository.findAll();

        return tasks.stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getDueDate(),
                        task.getCreatedAt(),
                        task.getUpdatedAt()
                ))
                .collect(Collectors.toList());

    }


    @Override
    @Transactional
    public TaskResponse updateTask(TaskCreateRequest request, Long id){
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (request.getTitle() != null) { task.setTitle(request.getTitle()); }
        if (request.getDescription() != null) { task.setDescription(request.getDescription()); }
        if (request.getPriority() != null) { task.setPriority(request.getPriority()); }
        if (request.getDueDate() != null) { task.setDueDate(request.getDueDate()); }

        taskRepository.save(task);

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId){
        if(taskRepository.existsById(taskId)){
            taskRepository.deleteById(taskId);
        } else {
            throw new RuntimeException("Task not found with id " + taskId);
        }
    }

}
