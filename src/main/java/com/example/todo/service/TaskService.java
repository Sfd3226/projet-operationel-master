package com.example.todo.service;

import com.example.todo.dto.TaskDto;

import java.util.List;

public interface TaskService {

    TaskDto createTask(TaskDto taskDto);

    List<TaskDto> getAllTasks();

    TaskDto getTaskById(Long id);

    TaskDto updateTask(Long id, TaskDto taskDto);

    TaskDto completeTask(Long id);

    void deleteTask(Long id);
}
