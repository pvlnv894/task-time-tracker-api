package org.example.tasktimetrackerapi.converter;

import org.example.tasktimetrackerapi.dto.TimeRecordDTO;
import org.example.tasktimetrackerapi.model.TimeRecord;

public class TimeRecordConverter {
    public static TimeRecordDTO toDTO(TimeRecord entity) {
        TimeRecordDTO dto = new TimeRecordDTO();
        dto.setId(entity.getId());
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setTaskId(entity.getTaskId());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setDescription(entity.getDescription());

        return dto;
    }

    public static TimeRecord toEntity(TimeRecordDTO dto) {
        TimeRecord entity = new TimeRecord();
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setTaskId(dto.getTaskId());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setDescription(dto.getDescription());

        return entity;
    }
}
