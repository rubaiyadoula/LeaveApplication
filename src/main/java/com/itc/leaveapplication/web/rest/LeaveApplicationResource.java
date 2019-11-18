package com.itc.leaveapplication.web.rest;

import com.itc.leaveapplication.domain.enumeration.LeaveStatus;
import com.itc.leaveapplication.service.LeaveApplicationService;
import com.itc.leaveapplication.service.dto.LeaveRecordDTO;
import com.itc.leaveapplication.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * REST controller for managing {@link com.itc.leaveapplication.service.LeaveApplicationService}.
 */
@RestController
@RequestMapping("/api")
public class LeaveApplicationResource {
    private final Logger log = LoggerFactory.getLogger(LeaveApplicationResource.class);
    private static final String ENTITY_NAME = "";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaveApplicationService leaveApplicationService;

    public LeaveApplicationResource(LeaveApplicationService leaveApplicationService) {
        this.leaveApplicationService = leaveApplicationService;
    }

    @PostMapping("/leaves")
    public ResponseEntity<LeaveRecordDTO> applyLeave(@RequestBody LeaveRecordDTO leaveRecord) throws URISyntaxException {
        if(leaveRecord.getId() != null) {
            throw new BadRequestAlertException("A new leaveRecord cannot already have an ID", "leave_record", "idexists");
        }
        LeaveRecordDTO result = leaveApplicationService.applyForLeave(leaveRecord);
        return ResponseEntity.created(new URI("/api/leaves/" + result.getId()))
            .body(result);
    }

    @GetMapping("/leave-status/{leaveId}")
    public ResponseEntity<Enum<LeaveStatus>> getLeaveStatus(@PathVariable Long leaveId) {
        log.debug("REST request to get Leave Status", leaveId);
        Enum<LeaveStatus> leaveStatus = leaveApplicationService.viewLeaveStatus(leaveId);
        return ResponseEntity.ok(leaveStatus);
    }

    @GetMapping("/casual-leave-balance/{employeeId}")
    public ResponseEntity<Integer> getCasualBalance(@PathVariable Long employeeId) {
        log.debug("REST request to get Casual Leave Balance", employeeId);
        int casualLeaves = leaveApplicationService.viewCasualLeaveBalance(employeeId);
        return ResponseEntity.ok(casualLeaves);
    }

    @GetMapping("/sick-leave-balance/{employeeId}")
    public ResponseEntity<Integer> getSickBalance(@PathVariable Long employeeId) {
        log.debug("REST request to get Sick Leave Balance", employeeId);
        int sickLeaves = leaveApplicationService.viewSickLeaveBalance(employeeId);
        return ResponseEntity.ok(sickLeaves);
    }

    @PutMapping("/leave-status/{employeeId}/{leaveId}")
    public ResponseEntity<Void> updateLeaveStatus(@PathVariable Long employeeId, @PathVariable Long leaveId) {
        log.debug("REST request to update leave status", employeeId, leaveId);
        leaveApplicationService.verifyOrDenyLeave(employeeId, leaveId);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, leaveId.toString())).build();
    }
}
