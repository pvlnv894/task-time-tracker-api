package org.example.tasktimetrackerapi.advice;

import org.example.tasktimetrackerapi.enums.ErrorCode;
import org.example.tasktimetrackerapi.exception.InvalidTimeRecordException;
import org.example.tasktimetrackerapi.exception.TaskNotFoundException;
import org.example.tasktimetrackerapi.exception.TimeRecordsNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.example.tasktimetrackerapi.dto.ErrorResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestBody(HttpMessageNotReadableException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorCode.INVALID_REQUEST_BODY,
                        "Invalid request format",
                        LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestParams(MethodArgumentTypeMismatchException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorCode.INVALID_REQUEST_PARAMS,
                        "Invalid params format",
                        LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidTimeRecordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTimeRange(InvalidTimeRecordException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ErrorCode.INVALID_TIME_RANGE,
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ErrorCode.TASK_NOT_FOUND,
                        e.getMessage(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(TimeRecordsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTimeRecordsNotFound(TimeRecordsNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ErrorCode.TIME_RECORDS_NOT_FOUND,
                        e.getMessage(),
                        LocalDateTime.now()));
    }
}
