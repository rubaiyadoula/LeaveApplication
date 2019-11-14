package com.itc.leaveapplication.service;

import com.itc.leaveapplication.domain.LeaveRecord;
import com.itc.leaveapplication.repository.LeaveRecordRepository;
import com.itc.leaveapplication.service.dto.LeaveRecordDTO;
import com.itc.leaveapplication.service.mapper.LeaveRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LeaveRecord}.
 */
@Service
@Transactional
public class LeaveRecordService {

    private final Logger log = LoggerFactory.getLogger(LeaveRecordService.class);

    private final LeaveRecordRepository leaveRecordRepository;

    private final LeaveRecordMapper leaveRecordMapper;

    public LeaveRecordService(LeaveRecordRepository leaveRecordRepository, LeaveRecordMapper leaveRecordMapper) {
        this.leaveRecordRepository = leaveRecordRepository;
        this.leaveRecordMapper = leaveRecordMapper;
    }

    /**
     * Save a leaveRecord.
     *
     * @param leaveRecordDTO the entity to save.
     * @return the persisted entity.
     */
    public LeaveRecordDTO save(LeaveRecordDTO leaveRecordDTO) {
        log.debug("Request to save LeaveRecord : {}", leaveRecordDTO);
        LeaveRecord leaveRecord = leaveRecordMapper.toEntity(leaveRecordDTO);
        leaveRecord = leaveRecordRepository.save(leaveRecord);
        return leaveRecordMapper.toDto(leaveRecord);
    }

    /**
     * Get all the leaveRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveRecords");
        return leaveRecordRepository.findAll(pageable)
            .map(leaveRecordMapper::toDto);
    }


    /**
     * Get one leaveRecord by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LeaveRecordDTO> findOne(Long id) {
        log.debug("Request to get LeaveRecord : {}", id);
        return leaveRecordRepository.findById(id)
            .map(leaveRecordMapper::toDto);
    }

    /**
     * Delete the leaveRecord by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LeaveRecord : {}", id);
        leaveRecordRepository.deleteById(id);
    }
}
