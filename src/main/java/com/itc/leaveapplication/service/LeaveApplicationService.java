package com.itc.leaveapplication.service;

import com.itc.leaveapplication.domain.enumeration.EmployeeRank;
import com.itc.leaveapplication.domain.enumeration.LeaveStatus;
import com.itc.leaveapplication.domain.enumeration.LeaveType;
import com.itc.leaveapplication.service.dto.EmployeeDTO;
import com.itc.leaveapplication.service.dto.LeaveRecordDTO;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public class LeaveApplicationService {
    private EmployeeService employeeService;
    private LeaveRecordService leaveRecordService;

    public LeaveApplicationService(EmployeeService employeeService, LeaveRecordService leaveRecordService) {
        this.employeeService = employeeService;
        this.leaveRecordService = leaveRecordService;
    }

    /// Employee
    /// All employees can apply for leave, view their applied leave's status and also check their remaining leaves.
    /// These operations can be done by any employees, be it developer, hr or manager.

    public void applyForLeave(LeaveRecordDTO leaveRecord) {
        leaveRecord.setStatus(LeaveStatus.PENDING);
    }

    public void viewLeaveStatus(Long leaveId) {
        Optional<LeaveRecordDTO> leaveRecord = leaveRecordService.findOne(leaveId);
        if(leaveRecord.get().getStatus().equals(LeaveStatus.VERIFIED) ||
            leaveRecord.get().getStatus().equals(LeaveStatus.DENIED)) {
            System.out.println(LeaveStatus.PENDING);
        } else {
            System.out.println(leaveRecord.get().getStatus());
        }
    }

    public void viewLeaveBalance(Long id) {
        Optional<EmployeeDTO> employee = employeeService.findOne(id);
        System.out.println("Remaining casual leave(s): " + employee.get().getCasualLeave());
        System.out.println("Remaining sick leave(s): " + employee.get().getSickLeave());
    }

    /// HR
    /// HR will verify leaves based on the amount of remaining leaves.
    /// In case, there aren't any remaining leaves, the leave will be denied.

    public void verifyOrDenyLeave(Long employeeId, Long leaveId) {
        Optional<LeaveRecordDTO> leaveRecord = leaveRecordService.findOne(leaveId);
        Optional<EmployeeDTO> hr = employeeService.findOne(employeeId);
        Optional<EmployeeDTO> employee = employeeService.findOne(leaveRecord.get().getEmployeeId());

        if((hr.get().getRank().equals(EmployeeRank.HR) &&
            leaveRecord.get().getLeaveCause() != null &&
            leaveRecord.get().getStatus().equals(LeaveStatus.PENDING)) &&
            (((leaveRecord.get().getType().equals(LeaveType.CASUAL)) && (employee.get().getCasualLeave() > 0)) ||
            ((leaveRecord.get().getType().equals(LeaveType.SICK)) && (employee.get().getSickLeave() > 0)))) {
            leaveRecord.get().setStatus(LeaveStatus.VERIFIED);
        } else {
            leaveRecord.get().setStatus(LeaveStatus.DENIED);
        }
    }

    /// Manager
    /// If HR has verified the leave, the Manager will most probably approve it too.
    /// In case, there are any active project the employee that applied for the leave is working on,
    /// the leave will be rejected. In this case, Manager will have to reject the leave manually.

    /// If HR has denied the leave, the Manager will reject it.
    /// In case, it's an emergency, based on the situation, the Manager can approve it manyally.

    /// Manager can also view all the leaves.

    public void approveOrRejectLeave(Long employeeId, Long leaveId) {
        Optional<LeaveRecordDTO> leaveRecord = leaveRecordService.findOne(leaveId);
        Optional<EmployeeDTO> employee = employeeService.findOne(leaveRecord.get().getEmployeeId());
        Optional<EmployeeDTO> manager = employeeService.findOne(employeeId);

        if(manager.get().getRank().equals(EmployeeRank.MANAGER)) {
            if(leaveRecord.get().getStatus().equals(LeaveStatus.VERIFIED)) {
                leaveRecord.get().setStatus(LeaveStatus.APPROVED); /// Approving the leave

                /// Reducing leave from total leave

                if(leaveRecord.get().getType().equals(LeaveType.CASUAL))
                    employee.get().setCasualLeave(employee.get().getCasualLeave() - 1);
                else if(leaveRecord.get().getType().equals(LeaveType.SICK))
                    employee.get().setSickLeave(employee.get().getSickLeave() - 1);
            }
            else if(leaveRecord.get().getStatus().equals(LeaveStatus.DENIED))
                leaveRecord.get().setStatus(LeaveStatus.REJECTED);
        } else {
            System.out.println("Unauthorized request!");
        }
    }

    public void viewLeaveList(Long id) {
        Optional<EmployeeDTO> manager = employeeService.findOne(id);
        System.out.println(leaveRecordService.findAll(PageRequest.of(1, 10)));
    }
}
