package org.example.tasktimetrackerapi.exception;

public class InvalidTimeRecordException extends RuntimeException {
    public InvalidTimeRecordException(String message) {
        super(message);
    }
}
