package com.example.todo.service;

import com.example.todo.dto.TaskDto;
import com.example.todo.entity.Status;
import com.example.todo.entity.Task;
import com.example.todo.exception.TaskNotFoundException;
import com.example.todo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void completeTaskShouldSetStatusCompleted() {
        Task task = Task.builder()
                .id(1L)
                .title("Task A")
                .description("Desc")
                .status(Status.IN_PROGRESS)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        TaskDto result = taskService.completeTask(1L);

        assertEquals(Status.COMPLETED, result.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    void getTaskByIdShouldThrowWhenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(99L));
    }
}
