package com.itc.leaveapplication.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

import com.itc.leaveapplication.domain.enumeration.LeaveType;

import com.itc.leaveapplication.domain.enumeration.LeaveStatus;

/**
 * A LeaveRecord.
 */
@Entity
@Table(name = "leave_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LeaveRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LeaveType type;

    @Column(name = "leave_start_date")
    private LocalDate leaveStartDate;

    @Column(name = "leave_end_date")
    private LocalDate leaveEndDate;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "leave_cause")
    private String leaveCause;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LeaveStatus status;

    @ManyToOne
    @JsonIgnoreProperties("leaveRecords")
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveType getType() {
        return type;
    }

    public LeaveRecord type(LeaveType type) {
        this.type = type;
        return this;
    }

    public void setType(LeaveType type) {
        this.type = type;
    }

    public LocalDate getLeaveStartDate() {
        return leaveStartDate;
    }

    public LeaveRecord leaveStartDate(LocalDate leaveStartDate) {
        this.leaveStartDate = leaveStartDate;
        return this;
    }

    public void setLeaveStartDate(LocalDate leaveStartDate) {
        this.leaveStartDate = leaveStartDate;
    }

    public LocalDate getLeaveEndDate() {
        return leaveEndDate;
    }

    public LeaveRecord leaveEndDate(LocalDate leaveEndDate) {
        this.leaveEndDate = leaveEndDate;
        return this;
    }

    public void setLeaveEndDate(LocalDate leaveEndDate) {
        this.leaveEndDate = leaveEndDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public LeaveRecord employeeId(Long employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getLeaveCause() {
        return leaveCause;
    }

    public LeaveRecord leaveCause(String leaveCause) {
        this.leaveCause = leaveCause;
        return this;
    }

    public void setLeaveCause(String leaveCause) {
        this.leaveCause = leaveCause;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public LeaveRecord status(LeaveStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LeaveRecord employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveRecord)) {
            return false;
        }
        return id != null && id.equals(((LeaveRecord) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "LeaveRecord{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", leaveStartDate='" + getLeaveStartDate() + "'" +
            ", leaveEndDate='" + getLeaveEndDate() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", leaveCause='" + getLeaveCause() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
