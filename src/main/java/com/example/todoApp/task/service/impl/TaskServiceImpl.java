package com.example.todoApp.task.service.impl;

import com.example.todoApp.task.model.TaskEntity;
import com.example.todoApp.task.model.TaskPriority;
import com.example.todoApp.task.model.TaskStatus;
import com.example.todoApp.task.model.dto.TaskCreateRequest;
import com.example.todoApp.task.model.dto.TaskResponse;
import com.example.todoApp.task.repository.TaskRepository;
import com.example.todoApp.task.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

        return mapToTaskResponse(saved);
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

        return mapToTaskResponse(task);
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

    @Override
    public List<TaskResponse> getTasks(TaskStatus status, TaskPriority priority, LocalDateTime dueDate) {
        List<TaskEntity> tasks;

        if (status != null && priority != null) {
            tasks = taskRepository.findByStatusAndPriority(status, priority);

        } else if (status != null) {
            tasks = taskRepository.findByStatus(status);

        } else if (priority != null) {
            tasks = taskRepository.findByPriority(priority);

        } else {
            tasks = taskRepository.findAll();
        }

        if (dueDate != null) {
            tasks = tasks.stream()
                    .filter(x -> x.getDueDate().isBefore(dueDate))
                    .toList();
        }

        return tasks.stream().map(this::mapToTaskResponse).toList();
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id " + id));
        return mapToTaskResponse(task);
    }

    private TaskResponse mapToTaskResponse(TaskEntity taskEntity){
        return new TaskResponse (
                taskEntity.getId(),
                taskEntity.getTitle(),
                taskEntity.getDescription(),
                taskEntity.getStatus(),
                taskEntity.getPriority(),
                taskEntity.getDueDate(),
                taskEntity.getCreatedAt(),
                taskEntity.getUpdatedAt()
        );
    }

}
