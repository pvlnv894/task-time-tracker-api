package org.example.tasktimetrackerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.tasktimetrackerapi.enums.ErrorCode;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private ErrorCode errorCode;
    private String message;
    private LocalDateTime timestamp;
}
