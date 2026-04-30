package org.example.tasktimetrackerapi.service;

import lombok.RequiredArgsConstructor;
import org.example.tasktimetrackerapi.dto.TimeRecordDTO;
import org.example.tasktimetrackerapi.exception.InvalidTimeRecordException;
import org.example.tasktimetrackerapi.exception.TaskNotFoundException;
import org.example.tasktimetrackerapi.exception.TimeRecordsNotFoundException;
import org.example.tasktimetrackerapi.mapper.TaskMapper;
import org.example.tasktimetrackerapi.model.TimeRecord;
import org.example.tasktimetrackerapi.mapper.TimeRecordMapper;
import org.example.tasktimetrackerapi.converter.TimeRecordConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeRecordService {

    private final TimeRecordMapper timeRecordMapper;
    private final TaskMapper taskMapper;

    @Transactional
    public void saveTimeRecord(TimeRecordDTO dto) {
        validateTime(dto.getStartTime(), dto.getEndTime());

        if (taskMapper.selectById(dto.getTaskId()) == null) {
            throw new TaskNotFoundException("Task with ID " + dto.getTaskId() + " not found");
        }

        TimeRecord entity = TimeRecordConverter.toEntity(dto);
        timeRecordMapper.insert(entity);
    }

    public List<TimeRecordDTO> getTimeRecordsFromTo(LocalDateTime startTime, LocalDateTime endTime, Long employeeId) {
        validateTime(startTime,endTime);

        List<TimeRecord> entityList = timeRecordMapper.selectFromTo(startTime, endTime, employeeId);

        if (entityList.isEmpty()) {
            throw new TimeRecordsNotFoundException("Time records not found for the given period");
        }

        List<TimeRecordDTO> dtoList = entityList.stream()
                .map(TimeRecordConverter::toDTO)
                .toList();

        return dtoList;
    }

    public void validateTime(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(LocalDateTime.now()) || endTime.isAfter(LocalDateTime.now()) || startTime.isAfter(endTime)) {
            throw new InvalidTimeRecordException("Invalid time range");
        }
    }
}
