package org.example.tasktimetrackerapi.unit.service;

import org.example.tasktimetrackerapi.dto.TimeRecordDTO;
import org.example.tasktimetrackerapi.exception.InvalidTimeRecordException;
import org.example.tasktimetrackerapi.exception.TimeRecordsNotFoundException;
import org.example.tasktimetrackerapi.mapper.TaskMapper;
import org.example.tasktimetrackerapi.mapper.TimeRecordMapper;
import org.example.tasktimetrackerapi.model.Task;
import org.example.tasktimetrackerapi.model.TimeRecord;
import org.example.tasktimetrackerapi.service.TimeRecordService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TimeRecordServiceTest {
    @Test
    void saveTimeRecord_shouldCallInsert_whenDtoIsValid() {
        ArgumentCaptor<TimeRecord> timeRecordCaptor = ArgumentCaptor.forClass(TimeRecord.class);

        TimeRecordDTO timeRecordDTO = new TimeRecordDTO();
        timeRecordDTO.setEmployeeId(1L);
        timeRecordDTO.setTaskId(1L);
        timeRecordDTO.setStartTime(LocalDateTime.parse("2020-02-02T20:20:20"));
        timeRecordDTO.setEndTime(LocalDateTime.parse("2020-02-02T20:30:20"));

        TimeRecordMapper timeRecordMapper = Mockito.mock(TimeRecordMapper.class);
        TaskMapper taskMapper = Mockito.mock(TaskMapper.class);
        Mockito.when(taskMapper.selectById(timeRecordDTO.getTaskId()))
                .thenReturn(new Task());

        TimeRecordService timeRecordService = new TimeRecordService(timeRecordMapper, taskMapper);

        timeRecordService.saveTimeRecord(timeRecordDTO);

        Mockito.verify(timeRecordMapper).insert(timeRecordCaptor.capture());

        TimeRecord actual = timeRecordCaptor.getValue();

        Assertions.assertEquals(timeRecordDTO.getEmployeeId(), actual.getEmployeeId());
        Assertions.assertEquals(timeRecordDTO.getTaskId(), actual.getTaskId());
        Assertions.assertEquals(timeRecordDTO.getStartTime(), actual.getStartTime());
        Assertions.assertEquals(timeRecordDTO.getEndTime(), actual.getEndTime());
    }

    @Test
    void saveTimeRecord_shouldThrowException_whenStartTimeIsInFuture() {
        TimeRecordDTO timeRecordDTO = new TimeRecordDTO();
        timeRecordDTO.setEmployeeId(1L);
        timeRecordDTO.setTaskId(1L);
        timeRecordDTO.setStartTime(LocalDateTime.parse("2029-02-02T20:20:20"));
        timeRecordDTO.setEndTime(LocalDateTime.parse("2020-02-02T20:30:20"));

        TimeRecordMapper timeRecordMapper = Mockito.mock(TimeRecordMapper.class);
        TaskMapper taskMapper = Mockito.mock(TaskMapper.class);
        TimeRecordService timeRecordService = new TimeRecordService(timeRecordMapper, taskMapper);

        InvalidTimeRecordException exception = Assertions.assertThrows(InvalidTimeRecordException.class, () -> {
            timeRecordService.saveTimeRecord(timeRecordDTO);
        });

        Assertions.assertEquals("Invalid time range", exception.getMessage());
    }

    @Test
    void getTimeRecordsFromTo_shouldReturnDtoList_whenRecordsExist() {
        TimeRecord timeRecord1 = new TimeRecord();
        timeRecord1.setId(1L);
        timeRecord1.setEmployeeId(1L);
        timeRecord1.setTaskId(1L);
        timeRecord1.setStartTime(LocalDateTime.parse("2020-02-02T20:20:20"));
        timeRecord1.setEndTime(LocalDateTime.parse("2020-02-02T20:30:20"));

        TimeRecord timeRecord2 = new TimeRecord();
        timeRecord2.setId(2L);
        timeRecord2.setEmployeeId(1L);
        timeRecord2.setTaskId(1L);
        timeRecord2.setStartTime(LocalDateTime.parse("2020-02-02T20:30:20"));
        timeRecord2.setEndTime(LocalDateTime.parse("2020-02-02T20:40:20"));

        List<TimeRecord> timeRecords = List.of(timeRecord1, timeRecord2);

        TimeRecordDTO timeRecordDTO1 = new TimeRecordDTO();
        timeRecordDTO1.setId(1L);
        timeRecordDTO1.setEmployeeId(1L);
        timeRecordDTO1.setTaskId(1L);
        timeRecordDTO1.setStartTime(LocalDateTime.parse("2020-02-02T20:20:20"));
        timeRecordDTO1.setEndTime(LocalDateTime.parse("2020-02-02T20:30:20"));

        TimeRecordDTO timeRecordDTO2 = new TimeRecordDTO();
        timeRecordDTO2.setId(2L);
        timeRecordDTO2.setEmployeeId(1L);
        timeRecordDTO2.setTaskId(1L);
        timeRecordDTO2.setStartTime(LocalDateTime.parse("2020-02-02T20:30:20"));
        timeRecordDTO2.setEndTime(LocalDateTime.parse("2020-02-02T20:40:20"));

        List<TimeRecordDTO> expectedList = List.of(timeRecordDTO1, timeRecordDTO2);

        LocalDateTime startTime = LocalDateTime.parse("2020-02-02T20:00:00");
        LocalDateTime endTime = LocalDateTime.parse("2020-02-02T21:00:00");
        Long employeeId = 1L;

        TimeRecordMapper timeRecordMapper = Mockito.mock(TimeRecordMapper.class);
        TaskMapper taskMapper = Mockito.mock(TaskMapper.class);
        Mockito.when(timeRecordMapper.selectFromTo(startTime, endTime, employeeId))
                .thenReturn(timeRecords);


        TimeRecordService timeRecordService = new TimeRecordService(timeRecordMapper, taskMapper);

        List<TimeRecordDTO> actualList = timeRecordService.getTimeRecordsFromTo(startTime, endTime, employeeId);

        assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }

    @Test
    void getTimeRecordsFromTo_shouldThrowException_whenStartTimeIsAfterEndTime() {
        LocalDateTime startTime = LocalDateTime.parse("2020-02-22T20:00:00");
        LocalDateTime endTime = LocalDateTime.parse("2020-02-02T21:00:00");
        Long employeeId = 1L;

        TimeRecordMapper timeRecordMapper = Mockito.mock(TimeRecordMapper.class);
        TaskMapper taskMapper = Mockito.mock(TaskMapper.class);
        Mockito.when(timeRecordMapper.selectFromTo(startTime, endTime, employeeId))
                .thenReturn(List.of());

        TimeRecordService timeRecordService = new TimeRecordService(timeRecordMapper, taskMapper);

        InvalidTimeRecordException exception = Assertions.assertThrows(InvalidTimeRecordException.class, () -> {
            timeRecordService.getTimeRecordsFromTo(startTime, endTime, employeeId);
        });

        Assertions.assertEquals("Invalid time range", exception.getMessage());
    }

    @Test
    void getTimeRecordsFromTo_shouldThrowNotFoundException_whenNoRecordsInPeriod() {
        LocalDateTime startTime = LocalDateTime.parse("2020-02-02T20:00:00");
        LocalDateTime endTime = LocalDateTime.parse("2020-02-02T21:00:00");
        Long employeeId = 1L;

        TimeRecordMapper timeRecordMapper = Mockito.mock(TimeRecordMapper.class);
        TaskMapper taskMapper = Mockito.mock(TaskMapper.class);
        Mockito.when(timeRecordMapper.selectFromTo(startTime, endTime, employeeId))
                .thenReturn(List.of());

        TimeRecordService timeRecordService = new TimeRecordService(timeRecordMapper, taskMapper);

        TimeRecordsNotFoundException exception = Assertions.assertThrows(TimeRecordsNotFoundException.class, () -> {
            timeRecordService.getTimeRecordsFromTo(startTime, endTime, employeeId);
        });

        Assertions.assertEquals("Time records not found for the given period", exception.getMessage());
    }
}
