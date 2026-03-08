package com.example.todo.mapper;

import com.example.todo.dto.TaskDto;
import com.example.todo.entity.Task;

public class TaskMapper {

    // Entity → DTO
    public static TaskDto toDto(Task task) {
        if (task == null) {
            return null;
        }

        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .build();
    }

    // DTO → Entity
    public static Task toEntity(TaskDto taskDto) {
        if (taskDto == null) {
            return null;
        }

        return Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .build();
    }
}