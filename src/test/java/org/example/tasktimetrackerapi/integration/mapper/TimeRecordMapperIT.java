package org.example.tasktimetrackerapi.integration.mapper;

import org.example.tasktimetrackerapi.converter.TaskConverter;
import org.example.tasktimetrackerapi.converter.TimeRecordConverter;
import org.example.tasktimetrackerapi.dto.TaskDTO;
import org.example.tasktimetrackerapi.dto.TimeRecordDTO;
import org.example.tasktimetrackerapi.enums.TaskStatus;
import org.example.tasktimetrackerapi.mapper.TaskMapper;
import org.example.tasktimetrackerapi.mapper.TimeRecordMapper;
import org.example.tasktimetrackerapi.model.Task;
import org.example.tasktimetrackerapi.model.TimeRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
@Transactional
public class TimeRecordMapperIT {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("task_db")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.sql.init.mode", () -> "always");
        registry.add("spring.sql.init.data-locations", () -> "file:init.sql");
    }

    @Autowired
    private TimeRecordMapper timeRecordMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Test
    void testInsertAndSelectFromTo() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("test-task");
        taskDTO.setDescription("test");
        taskDTO.setStatus(TaskStatus.NEW);

        Task taskEntity = TaskConverter.toEntity(taskDTO);
        taskMapper.insert(taskEntity);
        Long taskId = taskEntity.getId();

        TimeRecordDTO timeRecordDTO = new TimeRecordDTO();
        timeRecordDTO.setEmployeeId(1L);
        timeRecordDTO.setTaskId(taskId);
        timeRecordDTO.setStartTime(LocalDateTime.parse("2020-02-02T20:30:20"));
        timeRecordDTO.setEndTime(LocalDateTime.parse("2020-02-02T20:30:20"));

        TimeRecord timeRecordEntity = TimeRecordConverter.toEntity(timeRecordDTO);
        timeRecordMapper.insert(timeRecordEntity);

        LocalDateTime startTime = LocalDateTime.parse("2020-02-02T00:00:00");
        LocalDateTime endTime = LocalDateTime.parse("2020-02-03T00:00:00");
        Long employeeId = timeRecordDTO.getEmployeeId();

        List<TimeRecord> expectedList = List.of(timeRecordEntity);
        List<TimeRecord> actualList = timeRecordMapper.selectFromTo(startTime, endTime, employeeId);

        assertThat(actualList)
                .usingRecursiveComparison()
                .isEqualTo(expectedList);
    }
}
