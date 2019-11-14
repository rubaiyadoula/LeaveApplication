package com.itc.leaveapplication.service.mapper;

import com.itc.leaveapplication.domain.*;
import com.itc.leaveapplication.service.dto.EmployeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {


    @Mapping(target = "leaveRecords", ignore = true)
    @Mapping(target = "removeLeaveRecord", ignore = true)
    Employee toEntity(EmployeeDTO employeeDTO);

    default Employee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
