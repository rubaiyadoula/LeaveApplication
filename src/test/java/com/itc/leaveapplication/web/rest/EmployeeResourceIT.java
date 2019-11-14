package com.itc.leaveapplication.web.rest;

import com.itc.leaveapplication.LeaveApplicationApp;
import com.itc.leaveapplication.domain.Employee;
import com.itc.leaveapplication.repository.EmployeeRepository;
import com.itc.leaveapplication.service.EmployeeService;
import com.itc.leaveapplication.service.dto.EmployeeDTO;
import com.itc.leaveapplication.service.mapper.EmployeeMapper;
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

import com.itc.leaveapplication.domain.enumeration.EmployeeRank;
/**
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@SpringBootTest(classes = LeaveApplicationApp.class)
public class EmployeeResourceIT {

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final EmployeeRank DEFAULT_RANK = EmployeeRank.DEVELOPER;
    private static final EmployeeRank UPDATED_RANK = EmployeeRank.MANAGER;

    private static final Integer DEFAULT_CASUAL_LEAVE = 1;
    private static final Integer UPDATED_CASUAL_LEAVE = 2;

    private static final Integer DEFAULT_SICK_LEAVE = 1;
    private static final Integer UPDATED_SICK_LEAVE = 2;

    private static final LocalDate DEFAULT_JOIN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_JOIN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_EMPLOYED = false;
    private static final Boolean UPDATED_IS_EMPLOYED = true;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeService employeeService;

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

    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployeeResource employeeResource = new EmployeeResource(employeeService);
        this.restEmployeeMockMvc = MockMvcBuilders.standaloneSetup(employeeResource)
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
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .password(DEFAULT_PASSWORD)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS)
            .contact(DEFAULT_CONTACT)
            .rank(DEFAULT_RANK)
            .casualLeave(DEFAULT_CASUAL_LEAVE)
            .sickLeave(DEFAULT_SICK_LEAVE)
            .joinDate(DEFAULT_JOIN_DATE)
            .isEmployed(DEFAULT_IS_EMPLOYED);
        return employee;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .password(UPDATED_PASSWORD)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .contact(UPDATED_CONTACT)
            .rank(UPDATED_RANK)
            .casualLeave(UPDATED_CASUAL_LEAVE)
            .sickLeave(UPDATED_SICK_LEAVE)
            .joinDate(UPDATED_JOIN_DATE)
            .isEmployed(UPDATED_IS_EMPLOYED);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        restEmployeeMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testEmployee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployee.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testEmployee.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testEmployee.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testEmployee.getCasualLeave()).isEqualTo(DEFAULT_CASUAL_LEAVE);
        assertThat(testEmployee.getSickLeave()).isEqualTo(DEFAULT_SICK_LEAVE);
        assertThat(testEmployee.getJoinDate()).isEqualTo(DEFAULT_JOIN_DATE);
        assertThat(testEmployee.isIsEmployed()).isEqualTo(DEFAULT_IS_EMPLOYED);
    }

    @Test
    @Transactional
    public void createEmployeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // Create the Employee with an existing ID
        employee.setId(1L);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc.perform(get("/api/employees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.toString())))
            .andExpect(jsonPath("$.[*].casualLeave").value(hasItem(DEFAULT_CASUAL_LEAVE)))
            .andExpect(jsonPath("$.[*].sickLeave").value(hasItem(DEFAULT_SICK_LEAVE)))
            .andExpect(jsonPath("$.[*].joinDate").value(hasItem(DEFAULT_JOIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].isEmployed").value(hasItem(DEFAULT_IS_EMPLOYED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK.toString()))
            .andExpect(jsonPath("$.casualLeave").value(DEFAULT_CASUAL_LEAVE))
            .andExpect(jsonPath("$.sickLeave").value(DEFAULT_SICK_LEAVE))
            .andExpect(jsonPath("$.joinDate").value(DEFAULT_JOIN_DATE.toString()))
            .andExpect(jsonPath("$.isEmployed").value(DEFAULT_IS_EMPLOYED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .password(UPDATED_PASSWORD)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .contact(UPDATED_CONTACT)
            .rank(UPDATED_RANK)
            .casualLeave(UPDATED_CASUAL_LEAVE)
            .sickLeave(UPDATED_SICK_LEAVE)
            .joinDate(UPDATED_JOIN_DATE)
            .isEmployed(UPDATED_IS_EMPLOYED);
        EmployeeDTO employeeDTO = employeeMapper.toDto(updatedEmployee);

        restEmployeeMockMvc.perform(put("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testEmployee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEmployee.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testEmployee.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testEmployee.getCasualLeave()).isEqualTo(UPDATED_CASUAL_LEAVE);
        assertThat(testEmployee.getSickLeave()).isEqualTo(UPDATED_SICK_LEAVE);
        assertThat(testEmployee.getJoinDate()).isEqualTo(UPDATED_JOIN_DATE);
        assertThat(testEmployee.isIsEmployed()).isEqualTo(UPDATED_IS_EMPLOYED);
    }

    @Test
    @Transactional
    public void updateNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc.perform(put("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        restEmployeeMockMvc.perform(delete("/api/employees/{id}", employee.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = new Employee();
        employee1.setId(1L);
        Employee employee2 = new Employee();
        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);
        employee2.setId(2L);
        assertThat(employee1).isNotEqualTo(employee2);
        employee1.setId(null);
        assertThat(employee1).isNotEqualTo(employee2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeDTO.class);
        EmployeeDTO employeeDTO1 = new EmployeeDTO();
        employeeDTO1.setId(1L);
        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
        employeeDTO2.setId(employeeDTO1.getId());
        assertThat(employeeDTO1).isEqualTo(employeeDTO2);
        employeeDTO2.setId(2L);
        assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
        employeeDTO1.setId(null);
        assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(employeeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(employeeMapper.fromId(null)).isNull();
    }
}
