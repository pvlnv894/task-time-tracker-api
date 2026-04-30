package org.example.tasktimetrackerapi.converter;

import org.example.tasktimetrackerapi.dto.TaskDTO;
import org.example.tasktimetrackerapi.model.Task;

public class TaskConverter {
    public static TaskDTO toDTO(Task entity) {
        TaskDTO dto = new TaskDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());

        return dto;
    }

    public static Task toEntity(TaskDTO dto) {
        Task entity = new Task();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());

        return entity;
    }
}
