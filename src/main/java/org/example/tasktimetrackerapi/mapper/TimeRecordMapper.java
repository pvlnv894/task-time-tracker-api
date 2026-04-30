package org.example.tasktimetrackerapi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.tasktimetrackerapi.model.TimeRecord;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TimeRecordMapper {

    List<TimeRecord> selectFromTo(@Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime,
                                  @Param("employeeId") Long employeeId);

    void insert(TimeRecord timeRecord);
}
