package com.itc.leaveapplication.service.dto;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itc.leaveapplication.domain.enumeration.LeaveType;
import com.itc.leaveapplication.domain.enumeration.LeaveStatus;

/**
 * A DTO for the {@link com.itc.leaveapplication.domain.LeaveRecord} entity.
 */
public class LeaveRecordDTO implements Serializable {

    private Long id;

    private LeaveType type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate leaveStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate leaveEndDate;

    private Long employeeId;

    private String leaveCause;

    private LeaveStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveType getType() {
        return type;
    }

    public void setType(LeaveType type) {
        this.type = type;
    }

    public LocalDate getLeaveStartDate() {
        return leaveStartDate;
    }

    public void setLeaveStartDate(LocalDate leaveStartDate) {
        this.leaveStartDate = leaveStartDate;
    }

    public LocalDate getLeaveEndDate() {
        return leaveEndDate;
    }

    public void setLeaveEndDate(LocalDate leaveEndDate) {
        this.leaveEndDate = leaveEndDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getLeaveCause() {
        return leaveCause;
    }

    public void setLeaveCause(String leaveCause) {
        this.leaveCause = leaveCause;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LeaveRecordDTO leaveRecordDTO = (LeaveRecordDTO) o;
        if (leaveRecordDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), leaveRecordDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LeaveRecordDTO{" +
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
