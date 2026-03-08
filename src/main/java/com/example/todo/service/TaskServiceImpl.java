package com.example.todo.service;

import com.example.todo.dto.TaskDto;
import com.example.todo.entity.Status;
import com.example.todo.entity.Task;
import com.example.todo.exception.TaskNotFoundException;
import com.example.todo.mapper.TaskMapper;
import com.example.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task task = TaskMapper.toEntity(taskDto);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDto(savedTask);
    }

    @Override
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::toDto)
                .toList();
    }

    @Override
    public TaskDto getTaskById(Long id) {
        return TaskMapper.toDto(findTaskOrThrow(id));
    }

    @Override
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task existingTask = findTaskOrThrow(id);

        existingTask.setTitle(taskDto.getTitle());
        existingTask.setDescription(taskDto.getDescription());
        existingTask.setStatus(taskDto.getStatus());

        Task updatedTask = taskRepository.save(existingTask);
        return TaskMapper.toDto(updatedTask);
    }

    @Override
    public TaskDto completeTask(Long id) {
        Task existingTask = findTaskOrThrow(id);
        existingTask.setStatus(Status.COMPLETED);
        Task updatedTask = taskRepository.save(existingTask);
        return TaskMapper.toDto(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        Task existingTask = findTaskOrThrow(id);
        taskRepository.delete(existingTask);
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
