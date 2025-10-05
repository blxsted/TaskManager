package com.example.todoApp;

import com.example.todoApp.task.model.TaskEntity;
import com.example.todoApp.task.model.TaskPriority;
import com.example.todoApp.task.model.TaskStatus;
import com.example.todoApp.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TodoAppTaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void beforeEach() {
        taskRepository.deleteAll();
    }

    @Test
    void createTask_shouldReturn201AndTaskResponse() throws Exception {
        String json = """
                {
                    "title": "Integration Task",
                    "description": "Testbeschreibung",
                    "priority": "HIGH",
                    "dueDate": "2025-10-15T12:00:00"
                }
                
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Integration Task"))
                .andExpect(jsonPath("$.description").value("Testbeschreibung"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void getTasks_shouldReturnStoredTask() throws Exception {
        // Task in H2 speichern
        TaskEntity taskEntity = new TaskEntity();
        //taskEntity.setId(1L);
        taskEntity.setTitle("Stored Task");
        taskEntity.setDescription("Stored Task");
        taskEntity.setStatus(TaskStatus.IN_PROGRESS);
        taskEntity.setPriority(TaskPriority.MEDIUM);
        taskEntity.setDueDate(LocalDateTime.of(2025, 10, 20, 12, 0));

        taskRepository.save(taskEntity);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Stored Task"))
                .andExpect(jsonPath("$[0].description").value("Stored Task"))
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$[0].priority").value("MEDIUM"));

    }

}
