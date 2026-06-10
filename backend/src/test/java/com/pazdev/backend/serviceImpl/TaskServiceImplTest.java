package com.pazdev.backend.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pazdev.backend.dto.taskDTO.CreateTaskDTO;
import com.pazdev.backend.dto.taskDTO.TaskResponseDTO;
import com.pazdev.backend.dto.taskDTO.UpdateTaskDTO;
import com.pazdev.backend.entity.Task;
import com.pazdev.backend.entity.User;
import com.pazdev.backend.exception.ResourceNotFoundException;
import com.pazdev.backend.mapper.TaskMapper;
import com.pazdev.backend.repository.TaskRepository;
import com.pazdev.backend.repository.UserRepository;
import com.pazdev.backend.service.impl.TaskServiceImpl;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

        @Mock
        private TaskRepository taskRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private TaskMapper taskMapper;

        @InjectMocks
        private TaskServiceImpl taskService;

        @Test
        void shouldReturnTaskWhenIdExists() {

                Long taskId = 1L;

                Task task = new Task();
                task.setId(taskId);

                TaskResponseDTO response = new TaskResponseDTO();
                response.setId(taskId);

                when(taskRepository.findById(taskId))
                                .thenReturn(Optional.of(task));

                when(taskMapper.toResponse(task))
                                .thenReturn(response);

                TaskResponseDTO result = taskService.getTaskById(taskId);

                assertNotNull(result);
                assertEquals(response.getId(), result.getId());
        }

        @Test
        void shouldThrowExceptionWhenTaskNotExists() {

                Long taskId = 99L;

                when(taskRepository.findById(taskId))
                                .thenReturn(Optional.empty());

                assertThrows(
                                ResourceNotFoundException.class,
                                () -> taskService.getTaskById(taskId));
        }

        @Test
        void shouldReturnTasksWithParams() {

                String title = "Test";
                String status = "PENDING";
                Long taskId = 1L;

                Task task = new Task();
                task.setId(taskId);

                List<Task> tasks = new ArrayList<>();
                tasks.add(task);

                TaskResponseDTO response = new TaskResponseDTO();
                response.setId(taskId);
                List<TaskResponseDTO> responseList = new ArrayList<>();
                responseList.add(response);

                when(taskRepository.findByTitleContainingAndStatus(title, status))
                                .thenReturn(tasks);

                when(taskMapper.toResponse(task))
                                .thenReturn(response);

                List<TaskResponseDTO> result = taskService.getTasksByParams(title, status);

                assertNotNull(result.get(0));
                assertEquals(taskId, result.get(0).getId());
        }

        @Test
        void shouldReturnEmptyListWhenNoTasksFound() {

                String title = "Test";
                String status = "PENDING";

                when(taskRepository.findByTitleContainingAndStatus(title, status))
                                .thenReturn(new ArrayList<>());

                List<TaskResponseDTO> result = taskService.getTasksByParams(title, status);

                assertNotNull(result);
                assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnTasksWhenUserExists() {

                Long taskId = 1L;
                Long userId = 1L;

                Task task = new Task();
                task.setId(taskId);

                List<Task> tasks = new ArrayList<>();
                tasks.add(task);

                TaskResponseDTO response = new TaskResponseDTO();
                response.setId(taskId);

                when(userRepository.findById(userId))
                                .thenReturn(Optional.of(new User()));

                when(taskRepository.findByUserId(userId))
                                .thenReturn(tasks);

                when(taskMapper.toResponse(task))
                                .thenReturn(response);

                List<TaskResponseDTO> result = taskService.getTasksByUserId(userId);

                assertNotNull(result);
                assertEquals(taskId, result.get(0).getId());

        }

        @Test
        void shouldthrowExceptionWhenUserNotExists() {

                Long userId = 1L;

                TaskResponseDTO response = new TaskResponseDTO();
                List<TaskResponseDTO> responseList = new ArrayList<>();
                responseList.add(response);

                when(userRepository.findById(userId))
                                .thenReturn(Optional.empty());

                assertThrows(
                                ResourceNotFoundException.class,
                                () -> taskService.getTasksByUserId(userId));
        }

        @Test
        void shouldDeleteTask() {

                Long taskId = 1L;

                Task task = new Task();
                task.setId(taskId);

                when(taskRepository.findById(taskId))
                                .thenReturn(Optional.of(task));

                doNothing().when(taskRepository).deleteById(taskId);

                taskService.deleteTask(taskId);

                verify(taskRepository).deleteById(taskId);
        }

        @Test
        void shouldthrowExceptionInDeleteTask() {

                Long taskId = 1L;

                TaskResponseDTO response = new TaskResponseDTO();
                List<TaskResponseDTO> responseList = new ArrayList<>();
                responseList.add(response);

                when(taskRepository.findById(taskId))
                                .thenReturn(Optional.empty());

                assertThrows(
                                ResourceNotFoundException.class,
                                () -> taskService.deleteTask(taskId));
        }

        @Test
        void shouldCreateTask() {

                Long userId = 1L;
                String title = "New Task";
                String description = "Task Description";
                String status = "PENDING";

                CreateTaskDTO createTaskDTO = new CreateTaskDTO(title, description, status, userId);

                User user = new User();
                user.setId(userId);

                Task task = new Task();
                task.setId(1L);
                task.setTitle(title);
                task.setDescription(description);
                task.setStatus(status);
                task.setUser(user);

                TaskResponseDTO response = new TaskResponseDTO(1L, title, status);

                when(userRepository.findById(userId))
                                .thenReturn(Optional.of(user));

                when(taskRepository.save(any(Task.class)))
                                .thenReturn(task);

                when(taskMapper.toResponse(task))
                                .thenReturn(response);

                TaskResponseDTO result = taskService.createTask(createTaskDTO);

                assertNotNull(result);
                assertEquals(title, result.getTitle());
                assertEquals(status, result.getStatus());
        }

        @Test
        void shouldThrowExceptionWhenUserNotFoundInCreateTask() {

                Long userId = 99L;
                CreateTaskDTO createTaskDTO = new CreateTaskDTO("New Task", "Description", "PENDING", userId);

                when(userRepository.findById(userId))
                                .thenReturn(Optional.empty());

                assertThrows(
                                ResourceNotFoundException.class,
                                () -> taskService.createTask(createTaskDTO));
        }

        @Test
        void shouldUpdateTask() {

                Long taskId = 1L;
                String newTitle = "Updated Title";
                String newDescription = "Updated Description";
                String newStatus = "COMPLETED";

                UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO(newTitle, newDescription, newStatus);

                Task task = new Task();
                task.setId(taskId);
                task.setTitle("Old Title");
                task.setDescription("Old Description");
                task.setStatus("PENDING");

                Task updatedTask = new Task();
                updatedTask.setId(taskId);
                updatedTask.setTitle(newTitle);
                updatedTask.setDescription(newDescription);
                updatedTask.setStatus(newStatus);

                TaskResponseDTO response = new TaskResponseDTO(taskId, newTitle, newStatus);

                when(taskRepository.findById(taskId))
                                .thenReturn(Optional.of(task));

                when(taskRepository.save(any(Task.class)))
                                .thenReturn(updatedTask);

                when(taskMapper.toResponse(updatedTask))
                                .thenReturn(response);

                TaskResponseDTO result = taskService.updateTask(taskId, updateTaskDTO);

                assertNotNull(result);
                assertEquals(newTitle, result.getTitle());
                assertEquals(newStatus, result.getStatus());
        }

        @Test
        void shouldThrowExceptionWhenTaskNotFoundInUpdateTask() {

                Long taskId = 99L;
                UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO("New Title", "New Description", "COMPLETED");

                when(taskRepository.findById(taskId))
                                .thenReturn(Optional.empty());

                assertThrows(
                                ResourceNotFoundException.class,
                                () -> taskService.updateTask(taskId, updateTaskDTO));
        }

}