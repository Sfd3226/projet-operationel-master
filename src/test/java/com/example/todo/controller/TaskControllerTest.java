package com.example.todo.controller;

import com.example.todo.dto.TaskDto;
import com.example.todo.entity.Status;
import com.example.todo.exception.TaskNotFoundException;
import com.example.todo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    void completeTaskEndpointShouldReturnCompletedTask() throws Exception {
        TaskDto dto = TaskDto.builder()
                .id(1L)
                .title("Task A")
                .description("Desc")
                .status(Status.COMPLETED)
                .build();

        when(taskService.completeTask(1L)).thenReturn(dto);

        mockMvc.perform(patch("/api/tasks/1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void completeTaskEndpointShouldReturnNotFound() throws Exception {
        when(taskService.completeTask(123L)).thenThrow(new TaskNotFoundException(123L));

        mockMvc.perform(patch("/api/tasks/123/complete"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Task not found with id: 123"));
    }
}
