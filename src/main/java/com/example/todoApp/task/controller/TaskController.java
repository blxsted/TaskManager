package com.example.todoApp.task.controller;

import com.example.todoApp.task.model.TaskPriority;
import com.example.todoApp.task.model.TaskStatus;
import com.example.todoApp.task.model.dto.TaskCreateRequest;
import com.example.todoApp.task.model.dto.TaskResponse;
import com.example.todoApp.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@RequestBody TaskCreateRequest task) {
        return taskService.createTask(task);
    }

    @GetMapping
    public List<TaskResponse> getTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) LocalDateTime  dueDate
    ) {
        return taskService.getTasks(status, priority, dueDate);
    }

    @GetMapping("/{id}")
    public TaskResponse getTask(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @RequestBody TaskCreateRequest task,
            @PathVariable Long id){
        return taskService.updateTask(task, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }

}
