package com.itc.leaveapplication.web.rest;

import com.itc.leaveapplication.service.LeaveApplicationService;
import com.itc.leaveapplication.service.dto.LeaveRecordDTO;
import com.itc.leaveapplication.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Void> applyLeave(@RequestBody LeaveRecordDTO leaveRecord) {
        if(leaveRecord.getId() != null) {
            throw new BadRequestAlertException("A new leaveRecord cannot already have an ID", "leave_record", "idexists");
        }
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, leaveRecord.toString())).build();
    }
}
