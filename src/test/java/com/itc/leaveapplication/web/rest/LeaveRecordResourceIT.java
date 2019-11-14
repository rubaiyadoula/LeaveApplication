package com.itc.leaveapplication.web.rest;

import com.itc.leaveapplication.LeaveApplicationApp;
import com.itc.leaveapplication.domain.LeaveRecord;
import com.itc.leaveapplication.repository.LeaveRecordRepository;
import com.itc.leaveapplication.service.LeaveRecordService;
import com.itc.leaveapplication.service.dto.LeaveRecordDTO;
import com.itc.leaveapplication.service.mapper.LeaveRecordMapper;
import com.itc.leaveapplication.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.itc.leaveapplication.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.itc.leaveapplication.domain.enumeration.LeaveType;
import com.itc.leaveapplication.domain.enumeration.LeaveStatus;
/**
 * Integration tests for the {@link LeaveRecordResource} REST controller.
 */
@SpringBootTest(classes = LeaveApplicationApp.class)
public class LeaveRecordResourceIT {

    private static final LeaveType DEFAULT_TYPE = LeaveType.CASUAL;
    private static final LeaveType UPDATED_TYPE = LeaveType.SICK;

    private static final LocalDate DEFAULT_LEAVE_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LEAVE_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LEAVE_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LEAVE_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_EMPLOYEE_ID = 1L;
    private static final Long UPDATED_EMPLOYEE_ID = 2L;

    private static final String DEFAULT_LEAVE_CAUSE = "AAAAAAAAAA";
    private static final String UPDATED_LEAVE_CAUSE = "BBBBBBBBBB";

    private static final LeaveStatus DEFAULT_STATUS = LeaveStatus.PENDING;
    private static final LeaveStatus UPDATED_STATUS = LeaveStatus.VERIFIED;

    @Autowired
    private LeaveRecordRepository leaveRecordRepository;

    @Autowired
    private LeaveRecordMapper leaveRecordMapper;

    @Autowired
    private LeaveRecordService leaveRecordService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restLeaveRecordMockMvc;

    private LeaveRecord leaveRecord;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LeaveRecordResource leaveRecordResource = new LeaveRecordResource(leaveRecordService);
        this.restLeaveRecordMockMvc = MockMvcBuilders.standaloneSetup(leaveRecordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveRecord createEntity(EntityManager em) {
        LeaveRecord leaveRecord = new LeaveRecord()
            .type(DEFAULT_TYPE)
            .leaveStartDate(DEFAULT_LEAVE_START_DATE)
            .leaveEndDate(DEFAULT_LEAVE_END_DATE)
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .leaveCause(DEFAULT_LEAVE_CAUSE)
            .status(DEFAULT_STATUS);
        return leaveRecord;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveRecord createUpdatedEntity(EntityManager em) {
        LeaveRecord leaveRecord = new LeaveRecord()
            .type(UPDATED_TYPE)
            .leaveStartDate(UPDATED_LEAVE_START_DATE)
            .leaveEndDate(UPDATED_LEAVE_END_DATE)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .leaveCause(UPDATED_LEAVE_CAUSE)
            .status(UPDATED_STATUS);
        return leaveRecord;
    }

    @BeforeEach
    public void initTest() {
        leaveRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeaveRecord() throws Exception {
        int databaseSizeBeforeCreate = leaveRecordRepository.findAll().size();

        // Create the LeaveRecord
        LeaveRecordDTO leaveRecordDTO = leaveRecordMapper.toDto(leaveRecord);
        restLeaveRecordMockMvc.perform(post("/api/leave-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaveRecordDTO)))
            .andExpect(status().isCreated());

        // Validate the LeaveRecord in the database
        List<LeaveRecord> leaveRecordList = leaveRecordRepository.findAll();
        assertThat(leaveRecordList).hasSize(databaseSizeBeforeCreate + 1);
        LeaveRecord testLeaveRecord = leaveRecordList.get(leaveRecordList.size() - 1);
        assertThat(testLeaveRecord.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testLeaveRecord.getLeaveStartDate()).isEqualTo(DEFAULT_LEAVE_START_DATE);
        assertThat(testLeaveRecord.getLeaveEndDate()).isEqualTo(DEFAULT_LEAVE_END_DATE);
        assertThat(testLeaveRecord.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testLeaveRecord.getLeaveCause()).isEqualTo(DEFAULT_LEAVE_CAUSE);
        assertThat(testLeaveRecord.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createLeaveRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leaveRecordRepository.findAll().size();

        // Create the LeaveRecord with an existing ID
        leaveRecord.setId(1L);
        LeaveRecordDTO leaveRecordDTO = leaveRecordMapper.toDto(leaveRecord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveRecordMockMvc.perform(post("/api/leave-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaveRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeaveRecord in the database
        List<LeaveRecord> leaveRecordList = leaveRecordRepository.findAll();
        assertThat(leaveRecordList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLeaveRecords() throws Exception {
        // Initialize the database
        leaveRecordRepository.saveAndFlush(leaveRecord);

        // Get all the leaveRecordList
        restLeaveRecordMockMvc.perform(get("/api/leave-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].leaveStartDate").value(hasItem(DEFAULT_LEAVE_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].leaveEndDate").value(hasItem(DEFAULT_LEAVE_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID.intValue())))
            .andExpect(jsonPath("$.[*].leaveCause").value(hasItem(DEFAULT_LEAVE_CAUSE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getLeaveRecord() throws Exception {
        // Initialize the database
        leaveRecordRepository.saveAndFlush(leaveRecord);

        // Get the leaveRecord
        restLeaveRecordMockMvc.perform(get("/api/leave-records/{id}", leaveRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(leaveRecord.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.leaveStartDate").value(DEFAULT_LEAVE_START_DATE.toString()))
            .andExpect(jsonPath("$.leaveEndDate").value(DEFAULT_LEAVE_END_DATE.toString()))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID.intValue()))
            .andExpect(jsonPath("$.leaveCause").value(DEFAULT_LEAVE_CAUSE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLeaveRecord() throws Exception {
        // Get the leaveRecord
        restLeaveRecordMockMvc.perform(get("/api/leave-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeaveRecord() throws Exception {
        // Initialize the database
        leaveRecordRepository.saveAndFlush(leaveRecord);

        int databaseSizeBeforeUpdate = leaveRecordRepository.findAll().size();

        // Update the leaveRecord
        LeaveRecord updatedLeaveRecord = leaveRecordRepository.findById(leaveRecord.getId()).get();
        // Disconnect from session so that the updates on updatedLeaveRecord are not directly saved in db
        em.detach(updatedLeaveRecord);
        updatedLeaveRecord
            .type(UPDATED_TYPE)
            .leaveStartDate(UPDATED_LEAVE_START_DATE)
            .leaveEndDate(UPDATED_LEAVE_END_DATE)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .leaveCause(UPDATED_LEAVE_CAUSE)
            .status(UPDATED_STATUS);
        LeaveRecordDTO leaveRecordDTO = leaveRecordMapper.toDto(updatedLeaveRecord);

        restLeaveRecordMockMvc.perform(put("/api/leave-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaveRecordDTO)))
            .andExpect(status().isOk());

        // Validate the LeaveRecord in the database
        List<LeaveRecord> leaveRecordList = leaveRecordRepository.findAll();
        assertThat(leaveRecordList).hasSize(databaseSizeBeforeUpdate);
        LeaveRecord testLeaveRecord = leaveRecordList.get(leaveRecordList.size() - 1);
        assertThat(testLeaveRecord.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testLeaveRecord.getLeaveStartDate()).isEqualTo(UPDATED_LEAVE_START_DATE);
        assertThat(testLeaveRecord.getLeaveEndDate()).isEqualTo(UPDATED_LEAVE_END_DATE);
        assertThat(testLeaveRecord.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testLeaveRecord.getLeaveCause()).isEqualTo(UPDATED_LEAVE_CAUSE);
        assertThat(testLeaveRecord.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingLeaveRecord() throws Exception {
        int databaseSizeBeforeUpdate = leaveRecordRepository.findAll().size();

        // Create the LeaveRecord
        LeaveRecordDTO leaveRecordDTO = leaveRecordMapper.toDto(leaveRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveRecordMockMvc.perform(put("/api/leave-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaveRecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LeaveRecord in the database
        List<LeaveRecord> leaveRecordList = leaveRecordRepository.findAll();
        assertThat(leaveRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLeaveRecord() throws Exception {
        // Initialize the database
        leaveRecordRepository.saveAndFlush(leaveRecord);

        int databaseSizeBeforeDelete = leaveRecordRepository.findAll().size();

        // Delete the leaveRecord
        restLeaveRecordMockMvc.perform(delete("/api/leave-records/{id}", leaveRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaveRecord> leaveRecordList = leaveRecordRepository.findAll();
        assertThat(leaveRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveRecord.class);
        LeaveRecord leaveRecord1 = new LeaveRecord();
        leaveRecord1.setId(1L);
        LeaveRecord leaveRecord2 = new LeaveRecord();
        leaveRecord2.setId(leaveRecord1.getId());
        assertThat(leaveRecord1).isEqualTo(leaveRecord2);
        leaveRecord2.setId(2L);
        assertThat(leaveRecord1).isNotEqualTo(leaveRecord2);
        leaveRecord1.setId(null);
        assertThat(leaveRecord1).isNotEqualTo(leaveRecord2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveRecordDTO.class);
        LeaveRecordDTO leaveRecordDTO1 = new LeaveRecordDTO();
        leaveRecordDTO1.setId(1L);
        LeaveRecordDTO leaveRecordDTO2 = new LeaveRecordDTO();
        assertThat(leaveRecordDTO1).isNotEqualTo(leaveRecordDTO2);
        leaveRecordDTO2.setId(leaveRecordDTO1.getId());
        assertThat(leaveRecordDTO1).isEqualTo(leaveRecordDTO2);
        leaveRecordDTO2.setId(2L);
        assertThat(leaveRecordDTO1).isNotEqualTo(leaveRecordDTO2);
        leaveRecordDTO1.setId(null);
        assertThat(leaveRecordDTO1).isNotEqualTo(leaveRecordDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(leaveRecordMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(leaveRecordMapper.fromId(null)).isNull();
    }
}
