package org.example.tasktimetrackerapi.integration.mapper;

import org.example.tasktimetrackerapi.converter.TaskConverter;
import org.example.tasktimetrackerapi.dto.TaskDTO;
import org.example.tasktimetrackerapi.enums.TaskStatus;
import org.example.tasktimetrackerapi.mapper.TaskMapper;
import org.example.tasktimetrackerapi.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@Transactional
public class TaskMapperIT {
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
    private TaskMapper taskMapper;

    @Test
    void testInsertAndSelectById() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("test-task");
        taskDTO.setDescription("test");
        taskDTO.setStatus(TaskStatus.NEW);

        Task taskEntity = TaskConverter.toEntity(taskDTO);
        taskMapper.insert(taskEntity);

        Task saved = taskMapper.selectById(taskEntity.getId());

        Assertions.assertEquals(taskDTO.getName(), saved.getName());
        Assertions.assertEquals(taskDTO.getDescription(), saved.getDescription());
    }

    @Test
    void testInsertAndUpdateById() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("test-task");
        taskDTO.setDescription("test");
        taskDTO.setStatus(TaskStatus.NEW);

        Task taskEntity = TaskConverter.toEntity(taskDTO);
        taskMapper.insert(taskEntity);

        Task saved = taskMapper.selectById(taskEntity.getId());
        saved.setStatus(TaskStatus.DONE);

        taskMapper.updateById(saved);
        Task updated = taskMapper.selectById(taskEntity.getId());

        Assertions.assertEquals(saved.getStatus(), updated.getStatus());
    }
}
