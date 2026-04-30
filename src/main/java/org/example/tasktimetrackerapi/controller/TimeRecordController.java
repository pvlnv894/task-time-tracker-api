package org.example.tasktimetrackerapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tasktimetrackerapi.dto.ErrorResponse;
import org.example.tasktimetrackerapi.dto.TimeRecordDTO;
import org.example.tasktimetrackerapi.service.TimeRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/time-records")
@RequiredArgsConstructor
@Tag(name = "Time Records", description = "Employee time tracking endpoints")
public class TimeRecordController {

    private final TimeRecordService timeRecordService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Time Record")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Time record created successfully"),
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
            ),
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
    public void saveTimeRecord(@RequestBody @Valid TimeRecordDTO timeRecordDTO) {
        timeRecordService.saveTimeRecord(timeRecordDTO);
    }

    @GetMapping
    @Operation(summary = "Get time records")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Time records found"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request params",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class,
                                    example = "{\"errorCode\": \"INVALID_REQUEST_PARAMS\", \"message\": \"Invalid request params\", \"timeStamp\": \"2026-04-30T00:00:00\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid time range",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid Params",
                                            summary = "Format error",
                                            value = "{\"errorCode\": \"INVALID_REQUEST_PARAMS\", \"message\": \"Invalid request params format\", \"timeStamp\": \"2026-04-30T00:00:00\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Invalid Range",
                                            summary = "Date logic error",
                                            value = "{\"errorCode\": \"INVALID_TIME_RANGE\", \"message\": \"Start time must not be after end time\", \"timeStamp\": \"2026-04-30T00:00:00\"}"
                                    )
                            }
                    )
            )
    })
    public List<TimeRecordDTO> getTimeRecordsFromTo(@RequestParam LocalDateTime startTime,
                                                    @RequestParam LocalDateTime endTime,
                                                    @RequestParam Long employeeId) {
        return timeRecordService.getTimeRecordsFromTo(startTime, endTime, employeeId);
    }
}
