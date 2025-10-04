package com.example.todoApp.task.repository;

import com.example.todoApp.task.model.TaskEntity;
import com.example.todoApp.task.model.TaskPriority;
import com.example.todoApp.task.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByStatus(TaskStatus status);

    List<TaskEntity> findByPriority(TaskPriority priority);

    List<TaskEntity> findByDueDateBefore(LocalDateTime date);

}
