package com.itc.leaveapplication.service.mapper;

import com.itc.leaveapplication.domain.*;
import com.itc.leaveapplication.service.dto.LeaveRecordDTO;

import org.mapstruct.*;

import java.util.Optional;

/**
 * Mapper for the entity {@link LeaveRecord} and its DTO {@link LeaveRecordDTO}.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface LeaveRecordMapper extends EntityMapper<LeaveRecordDTO, LeaveRecord> {

    @Mapping(source = "employee.id", target = "employeeId")
    LeaveRecordDTO toDto(LeaveRecord leaveRecord);

    @Mapping(source = "employeeId", target = "employee")
    LeaveRecord toEntity(LeaveRecordDTO leaveRecordDTO);

    default LeaveRecord fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeaveRecord leaveRecord = new LeaveRecord();
        leaveRecord.setId(id);
        return leaveRecord;
    }
}
