package org.example.tasktimetrackerapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.tasktimetrackerapi.mapper")
public class TaskTimeTrackerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskTimeTrackerApiApplication.class, args);
    }

}
