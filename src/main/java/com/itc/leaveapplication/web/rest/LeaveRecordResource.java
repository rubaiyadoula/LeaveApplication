package com.itc.leaveapplication.web.rest;

import com.itc.leaveapplication.service.LeaveRecordService;
import com.itc.leaveapplication.web.rest.errors.BadRequestAlertException;
import com.itc.leaveapplication.service.dto.LeaveRecordDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.itc.leaveapplication.domain.LeaveRecord}.
 */
@RestController
@RequestMapping("/api")
public class LeaveRecordResource {

    private final Logger log = LoggerFactory.getLogger(LeaveRecordResource.class);

    private static final String ENTITY_NAME = "leaveRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaveRecordService leaveRecordService;

    public LeaveRecordResource(LeaveRecordService leaveRecordService) {
        this.leaveRecordService = leaveRecordService;
    }

    /**
     * {@code POST  /leave-records} : Create a new leaveRecord.
     *
     * @param leaveRecordDTO the leaveRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaveRecordDTO, or with status {@code 400 (Bad Request)} if the leaveRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leave-records")
    public ResponseEntity<LeaveRecordDTO> createLeaveRecord(@RequestBody LeaveRecordDTO leaveRecordDTO) throws URISyntaxException {
        log.debug("REST request to save LeaveRecord : {}", leaveRecordDTO);
        if (leaveRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaveRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaveRecordDTO result = leaveRecordService.save(leaveRecordDTO);
        return ResponseEntity.created(new URI("/api/leave-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leave-records} : Updates an existing leaveRecord.
     *
     * @param leaveRecordDTO the leaveRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveRecordDTO,
     * or with status {@code 400 (Bad Request)} if the leaveRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaveRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leave-records")
    public ResponseEntity<LeaveRecordDTO> updateLeaveRecord(@RequestBody LeaveRecordDTO leaveRecordDTO) throws URISyntaxException {
        log.debug("REST request to update LeaveRecord : {}", leaveRecordDTO);
        if (leaveRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LeaveRecordDTO result = leaveRecordService.save(leaveRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, leaveRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /leave-records} : get all the leaveRecords.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaveRecords in body.
     */
    @GetMapping("/leave-records")
    public ResponseEntity<List<LeaveRecordDTO>> getAllLeaveRecords(Pageable pageable) {
        log.debug("REST request to get a page of LeaveRecords");
        Page<LeaveRecordDTO> page = leaveRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leave-records/:id} : get the "id" leaveRecord.
     *
     * @param id the id of the leaveRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaveRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leave-records/{id}")
    public ResponseEntity<LeaveRecordDTO> getLeaveRecord(@PathVariable Long id) {
        log.debug("REST request to get LeaveRecord : {}", id);
        Optional<LeaveRecordDTO> leaveRecordDTO = leaveRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaveRecordDTO);
    }

    /**
     * {@code DELETE  /leave-records/:id} : delete the "id" leaveRecord.
     *
     * @param id the id of the leaveRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/leave-records/{id}")
    public ResponseEntity<Void> deleteLeaveRecord(@PathVariable Long id) {
        log.debug("REST request to delete LeaveRecord : {}", id);
        leaveRecordService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
