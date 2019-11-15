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

    @PostMapping("/createLeaves")
    public ResponseEntity<LeaveRecordDTO> applyLeave(@RequestBody LeaveRecordDTO leaveRecord) throws URISyntaxException {
        if(leaveRecord.getId() != null) {
            throw new BadRequestAlertException("A new leaveRecord cannot already have an ID", "leave_record", "idexists");
        }
        LeaveRecordDTO result = leaveApplicationService.applyForLeave(leaveRecord);
        return ResponseEntity.created(new URI("/api/leaves/" + result.getId()))
            .body(result);
    }

    @GetMapping("/getLeaveStatus")
    public ResponseEntity<Enum<LeaveStatus>> viewLeaveStatus(@PathVariable Long leaveId) {
        // Continue from here
        log.debug("REST request to get Leave Status", leaveId);
        Enum<LeaveStatus> leaveStatus = leaveApplicationService.viewLeaveStatus(leaveId);
        return ResponseEntity.ok(leaveStatus);
    }
}
