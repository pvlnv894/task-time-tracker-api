package org.example.tasktimetrackerapi.service;

import lombok.RequiredArgsConstructor;
import org.example.tasktimetrackerapi.dto.TaskDTO;
import org.example.tasktimetrackerapi.exception.TaskNotFoundException;
import org.example.tasktimetrackerapi.model.Task;
import org.example.tasktimetrackerapi.enums.TaskStatus;
import org.example.tasktimetrackerapi.converter.TaskConverter;
import org.example.tasktimetrackerapi.mapper.TaskMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;

    @Transactional
    public void saveTask(TaskDTO dto) {
        Task entity = TaskConverter.toEntity(dto);
        entity.setStatus(TaskStatus.NEW);
        taskMapper.insert(entity);
    }

    public TaskDTO getTaskById(Long id) {
        Task task = taskMapper.selectById(id);
        if (task == null) {
            throw new TaskNotFoundException("Task with ID " + id + " not found");
        }
        return TaskConverter.toDTO(task);
    }

    @Transactional
    public void changeTaskStatusById(Long id, TaskStatus status) {
        Task task = taskMapper.selectById(id);
        if (task == null) {
            throw new TaskNotFoundException("Task with ID " + id + " not found");
        }

        task.setStatus(status);
        taskMapper.updateById(task);
    }
}
