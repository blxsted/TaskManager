package com.example.todoApp;

import com.example.todoApp.task.model.TaskEntity;
import com.example.todoApp.task.model.TaskPriority;
import com.example.todoApp.task.model.TaskStatus;
import com.example.todoApp.task.model.dto.TaskCreateRequest;
import com.example.todoApp.task.model.dto.TaskResponse;
import com.example.todoApp.task.repository.TaskRepository;
import com.example.todoApp.task.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.todoApp.task.model.TaskStatus.OPEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class TodoAppApplicationTests {

	@Mock
	private TaskRepository taskRepository;

	@InjectMocks
	private TaskServiceImpl taskService;

	private TaskCreateRequest request;
	private TaskEntity mockEntity;
	private TaskEntity openTask;
    private LocalDateTime due;

	@BeforeEach
	void setUp() {
		due = LocalDateTime.of(2025, 10, 11, 12, 0);

		request = new TaskCreateRequest();
		request.setTitle("Test Task");
		request.setDescription("Beschreibung");
		request.setPriority(TaskPriority.HIGH);
		request.setDueDate(due);

		mockEntity = new TaskEntity();
		mockEntity.setId(1L);
		mockEntity.setTitle("OldTitle");
		mockEntity.setDescription("OldDescription");
		mockEntity.setPriority(TaskPriority.LOW);
		mockEntity.setStatus(OPEN);
		mockEntity.setDueDate(LocalDateTime.of(2025, 10, 5, 12, 0));

		openTask = new TaskEntity();
		openTask.setId(1L);
		openTask.setTitle("Open Task");
		openTask.setDescription("Open Task");
		openTask.setStatus(OPEN);

        TaskEntity closedTask = new TaskEntity();
		closedTask.setId(1L);
		closedTask.setTitle("Closed Task");
		closedTask.setDescription("Closed Task");
		closedTask.setStatus(TaskStatus.COMPLETED);
	}

	@Test
	void createTask_shouldCreateTaskResponse() {
		// given
		when(taskRepository.save(any(TaskEntity.class))).thenAnswer(i ->{
			TaskEntity t = i.getArgument(0);
			t.setId(1L);
			return t;
		});

		// when
		TaskResponse taskResponse = taskService.createTask(request);

		// then
		assertNotNull(taskResponse.getId());
		assertEquals("Test Task", taskResponse.getTitle());
		assertEquals("Beschreibung", taskResponse.getDescription());
		assertEquals(TaskPriority.HIGH, taskResponse.getPriority());
		assertEquals(OPEN, taskResponse.getStatus());
		assertEquals(due, taskResponse.getDueDate());

		// verify: sicherstellen, dass save aufgerufen wurde
		verify(taskRepository, times(1)).save(any(TaskEntity.class));
	}

	@Test
	void updateTask_shouldUpdateTaskResponse() {
		// given
		when(taskRepository.findById(1L)).thenReturn(Optional.of(mockEntity));
		when(taskRepository.save(any(TaskEntity.class))).thenAnswer(i -> i.getArguments()[0]);

		// when
		TaskResponse response = taskService.updateTask(request, 1L);

		// then
		assertNotNull(response.getId());
		assertEquals("Test Task", response.getTitle());
		assertEquals("Beschreibung", response.getDescription());
		assertEquals(TaskPriority.HIGH, response.getPriority());
		assertEquals(OPEN, response.getStatus());
		assertEquals(due, response.getDueDate());

		verify(taskRepository, times(1)).findById(1L);
		verify(taskRepository, times(1)).save(mockEntity);

	}

	@Test
	void deleteTask_shouldDeleteTaskResponse() {
		// given
		when(taskRepository.existsById(1L)).thenReturn(true);

		// when
		taskService.deleteTask(1L);

		// then
		verify(taskRepository, times(1)).existsById(1L);
		verify(taskRepository, times(1)).deleteById(1L);
	}

	@Test
	void deleteTask_shouldThrowExceptionWhenNotFound() {
		// given
		when(taskRepository.existsById(1L)).thenReturn(false);

		// when + then
		assertThrows(RuntimeException.class, () -> taskService.deleteTask(1L));

		verify(taskRepository, times(1)).existsById(1L);
		verify(taskRepository, never()).deleteById(anyLong());
	}

	// analog Priority & Due Date
	@Test
	void getTask_shouldReturnTasksFilteredByStatus() {
		// given
		when(taskRepository.findByStatus(OPEN)).thenReturn(List.of(openTask));

		//when
		List<TaskResponse> result = taskService.getTasks(OPEN, null, null);

		assertEquals(1, result.size());
		assertEquals("Open Task", result.getFirst().getTitle());
		assertEquals(OPEN, result.getFirst().getStatus());

		verify(taskRepository, times(1)).findByStatus(OPEN);
	}

	@Test
	void getAllTasks_shouldReturnAllTasks() {
		//given
		when(taskRepository.findAll()).thenReturn(List.of(mockEntity));

		//when
		List<TaskResponse> result = taskService.getTasks(null, null, null);

		assertEquals(1, result.size());
		assertEquals(OPEN, result.getFirst().getStatus());

		verify(taskRepository, times(1)).findAll();

	}
}
