package org.example.tasktimetrackerapi.exception;

public class TimeRecordsNotFoundException extends RuntimeException {
    public TimeRecordsNotFoundException(String message) {
        super(message);
    }
}
