package com.itc.leaveapplication.service;

import com.itc.leaveapplication.domain.Employee;
import com.itc.leaveapplication.domain.LeaveRecord;
import com.itc.leaveapplication.domain.enumeration.EmployeeRank;
import com.itc.leaveapplication.domain.enumeration.LeaveStatus;
import com.itc.leaveapplication.domain.enumeration.LeaveType;
import com.itc.leaveapplication.service.dto.EmployeeDTO;
import com.itc.leaveapplication.service.dto.LeaveRecordDTO;
import com.itc.leaveapplication.service.mapper.EmployeeMapper;
import com.itc.leaveapplication.service.mapper.LeaveRecordMapper;
import com.itc.leaveapplication.web.rest.EmployeeResource;
import com.itc.leaveapplication.web.rest.LeaveRecordResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.Optional;

@Service
@Transactional
public class LeaveApplicationService {
    private EmployeeResource employeeResource;
    private LeaveRecordResource leaveRecordResource;
    private EmployeeService employeeService;
    private LeaveRecordService leaveRecordService;
    private EmployeeMapper employeeMapper;
    private LeaveRecordMapper leaveRecordMapper;

    public LeaveApplicationService(
        EmployeeResource employeeResource,
        LeaveRecordResource leaveRecordResource,
        EmployeeService employeeService,
        LeaveRecordService leaveRecordService,
        EmployeeMapper employeeMapper,
        LeaveRecordMapper leaveRecordMapper) {
        this.employeeResource = employeeResource;
        this.leaveRecordResource = leaveRecordResource;
        this.employeeService = employeeService;
        this.leaveRecordService = leaveRecordService;
        this.employeeMapper = employeeMapper;
        this.leaveRecordMapper = leaveRecordMapper;
    }

    /**
     *
     * Employees can apply for Leave.
     * The Leave Status will be changed to PENDING.
     * @param leaveRecord the leaveRecordDTO to create
     */

    public LeaveRecordDTO applyForLeave(LeaveRecordDTO leaveRecord) {
        try {
            leaveRecordResource.createLeaveRecord(leaveRecord);
            leaveRecord.setStatus(LeaveStatus.PENDING);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return leaveRecord;
    }

    /**
     *
     * Employees can view their leave status.
     * As oppose to not expose the status while HR & Manager are reviewing the leave application,
     * the leave status will be shown PENDING until APPROVED or REJECTED.
     * @param leaveId the leaveId to track leaveStatus
     */

    @Transactional(readOnly = true)
    public Enum<LeaveStatus> viewLeaveStatus(Long leaveId) {
        Optional<LeaveRecordDTO> leaveRecord = leaveRecordService.findOne(leaveId);
//        if(!leaveRecord.get().getStatus().equals(LeaveStatus.APPROVED) ||
//            !leaveRecord.get().getStatus().equals(LeaveStatus.REJECTED)) {
//            return LeaveStatus.PENDING;
//        } else {
        return leaveRecord.get().getStatus();
//        }
    }

    /**
     *
     * Employees can view their Leave Balance, both CASUAL & SICK.
     * @param id the employeeId to track the employee's leave balance.
     */

    @Transactional(readOnly = true)
    public int viewCasualLeaveBalance(Long id) {
        Optional<EmployeeDTO> employee = employeeService.findOne(id);
        return employee.get().getCasualLeave();
    }

    @Transactional(readOnly = true)
    public int viewSickLeaveBalance(Long id) {
        Optional<EmployeeDTO> employee = employeeService.findOne(id);
        return employee.get().getSickLeave();
    }

    /**
     *
     * HR will verify the leave if there are any remaining leaves.
     * If no remaining leaves are available, the leave will be denied.
     * @param employeeId HR's ID for authorization
     * @param leaveId leaveId to track the leaves
     */

    public void verifyOrDenyLeave(Long employeeId, Long leaveId) {
        Optional<LeaveRecordDTO> leaveRecordDTOOptional = leaveRecordService.findOne(leaveId);
        LeaveRecordDTO leaveRecordDTO = leaveRecordDTOOptional.get();

        Optional<EmployeeDTO> hrDTO = employeeService.findOne(employeeId);
        Employee hr = employeeMapper.toEntity(hrDTO.get());

        Optional<EmployeeDTO> employeeDTO = employeeService.findOne(leaveRecordDTO.getEmployeeId());
        Employee employee = employeeMapper.toEntity(employeeDTO.get());

        if((hr.getRank().equals(EmployeeRank.HR) &&
            leaveRecordDTO.getLeaveCause() != null &&
            leaveRecordDTO.getStatus().equals(LeaveStatus.PENDING)) &&
            (((leaveRecordDTO.getType().equals(LeaveType.CASUAL)) && (employee.getCasualLeave() > 0)) ||
            ((leaveRecordDTO.getType().equals(LeaveType.SICK)) && (employee.getSickLeave() > 0)))) {
            leaveRecordDTO.setStatus(LeaveStatus.VERIFIED);
        } else {
            leaveRecordDTO.setStatus(LeaveStatus.DENIED);
        }
        LeaveRecord leaveRecord = leaveRecordMapper.toEntity(leaveRecordDTO);
    }

    /**
     *
     * Manager will APPROVE or REJECT the leave based on HR's decision.
     * @param employeeId to authorize manager's rank.
     * @param leaveId to track the leave.
     */

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

    /**
     *
     * Manager can view all the leaves.
     * @param id to authorize the view request.
     */

    public void viewLeaveList(Long id) {
        Optional<EmployeeDTO> manager = employeeService.findOne(id);
        System.out.println(leaveRecordService.findAll(PageRequest.of(0, 10)));
    }
}
