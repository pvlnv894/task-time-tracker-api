package org.example.tasktimetrackerapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tasktimetrackerapi.dto.ErrorResponse;
import org.example.tasktimetrackerapi.dto.TaskDTO;
import org.example.tasktimetrackerapi.dto.TaskStatusDTO;
import org.example.tasktimetrackerapi.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management endpoints")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Task")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class,
                                    example = "{\"errorCode\": \"INVALID_REQUEST_BODY\", \"message\": \"Invalid request format\", \"timeStamp\": \"2026-04-30T00:00:00\"}"
                            )
                    )
            )
    })
    public void saveTask(@RequestBody @Valid TaskDTO taskDTO) {
        taskService.saveTask(taskDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class,
                                    example = "{\"errorCode\": \"TASK_NOT_FOUND\", \"message\": \"Task with ID 1000 not found\", \"timeStamp\": \"2026-04-30T00:00:00\"}"
                            )
                    )
            )
    })
    public TaskDTO getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update task status by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task status updated successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Task not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class,
                                    example = "{\"errorCode\": \"TASK_NOT_FOUND\", \"message\": \"Task with ID 1000 not found\", \"timeStamp\": \"2026-04-30T00:00:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class,
                                    example = "{\"errorCode\": \"INVALID_REQUEST_BODY\", \"message\": \"Invalid request format\", \"timeStamp\": \"2026-04-30T00:00:00\"}"
                            )
                    )
            )
    })
    public void changeTaskStatusById(@PathVariable Long id, @RequestBody @Valid TaskStatusDTO taskStatusDTO) {
        taskService.changeTaskStatusById(id, taskStatusDTO.getStatus());
    }

}
