package org.example.tasktimetrackerapi.unit.service;

import org.example.tasktimetrackerapi.dto.TaskDTO;
import org.example.tasktimetrackerapi.enums.TaskStatus;
import org.example.tasktimetrackerapi.exception.TaskNotFoundException;
import org.example.tasktimetrackerapi.mapper.TaskMapper;
import org.example.tasktimetrackerapi.model.Task;
import org.example.tasktimetrackerapi.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class TaskServiceTest {
    @Test
    void saveTask_shouldCallInsert_whenDtoIsValid() {
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setName("test-task");

        TaskMapper mapper = Mockito.mock(TaskMapper.class);
        TaskService taskService = new TaskService(mapper);

        taskService.saveTask(taskDTO);

        Mockito.verify(mapper).insert(taskCaptor.capture());

        Task actual = taskCaptor.getValue();

        Assertions.assertEquals(taskDTO.getName(), actual.getName());
    }

    @Test
    void getTaskById_shouldReturnTaskDto_whenTaskExists() {
        long taskId = 1L;

        Task task = new Task();
        task.setId(taskId);
        task.setName("test-task");

        TaskMapper mapper = Mockito.mock(TaskMapper.class);
        Mockito.when(mapper.selectById(Mockito.any(Long.class)))
                .thenReturn(task);

        TaskService taskService = new TaskService(mapper);

        TaskDTO actual = taskService.getTaskById(taskId);

        Assertions.assertEquals(task.getId(), actual.getId());
        Assertions.assertEquals(task.getName(), actual.getName());
    }

    @Test
    void getTaskById_shouldThrowNotFoundException_whenTaskDoesNotExist() {
        long taskId = 1L;

        TaskMapper mapper = Mockito.mock(TaskMapper.class);
        Mockito.when(mapper.selectById(Mockito.any(Long.class)))
                .thenReturn(null);

        TaskService taskService = new TaskService(mapper);

        Assertions.assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(taskId);
        });
    }

    @Test
    void changeTaskStatusById_shouldUpdateStatus_whenTaskExists() {
        long taskId = 1L;
        TaskStatus taskStatus = TaskStatus.DONE;

        Task task = new Task();
        task.setId(taskId);
        task.setName("test-task");

        Task expected = new Task();
        expected.setId(taskId);
        expected.setName("test-task");
        expected.setStatus(TaskStatus.DONE);

        TaskMapper mapper = Mockito.mock(TaskMapper.class);
        Mockito.when(mapper.selectById(Mockito.any(Long.class)))
                .thenReturn(task);

        TaskService taskService = new TaskService(mapper);

        taskService.changeTaskStatusById(taskId, taskStatus);

        Assertions.assertEquals(task.getId(), expected.getId());
        Assertions.assertEquals(task.getName(), expected.getName());
        Assertions.assertEquals(task.getStatus(), expected.getStatus());
    }

    @Test
    void changeTaskStatusById_shouldThrowNotFoundException_whenTaskDoesNotExist() {
        long taskId = 1L;
        TaskStatus taskStatus = TaskStatus.DONE;

        TaskMapper mapper = Mockito.mock(TaskMapper.class);
        Mockito.when(mapper.selectById(Mockito.any(Long.class)))
                .thenReturn(null);

        TaskService taskService = new TaskService(mapper);

        Assertions.assertThrows(TaskNotFoundException.class, () -> {
            taskService.changeTaskStatusById(taskId, taskStatus);
        });
    }
}
