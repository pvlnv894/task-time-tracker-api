package org.example.tasktimetrackerapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.tasktimetrackerapi.enums.TaskStatus;

@Getter
@Setter
@NoArgsConstructor
public class Task {
    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
}
