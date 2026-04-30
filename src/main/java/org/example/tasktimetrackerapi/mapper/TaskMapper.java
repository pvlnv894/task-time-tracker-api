package org.example.tasktimetrackerapi.mapper;

import org.apache.ibatis.annotations.*;
import org.example.tasktimetrackerapi.model.Task;

@Mapper
public interface TaskMapper {

    Task selectById(Long id);

    void insert(Task task);

    void updateById(Task task);
}
