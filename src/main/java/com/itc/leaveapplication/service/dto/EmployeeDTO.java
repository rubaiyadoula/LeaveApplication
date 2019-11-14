package com.itc.leaveapplication.service.dto;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import com.itc.leaveapplication.domain.enumeration.EmployeeRank;

/**
 * A DTO for the {@link com.itc.leaveapplication.domain.Employee} entity.
 */
public class EmployeeDTO implements Serializable {

    private Long id;

    private String password;

    private String name;

    private String email;

    private String address;

    private String contact;

    private EmployeeRank rank;

    private Integer casualLeave;

    private Integer sickLeave;

    private LocalDate joinDate;

    private Boolean isEmployed;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public EmployeeRank getRank() {
        return rank;
    }

    public void setRank(EmployeeRank rank) {
        this.rank = rank;
    }

    public Integer getCasualLeave() {
        return casualLeave;
    }

    public void setCasualLeave(Integer casualLeave) {
        this.casualLeave = casualLeave;
    }

    public Integer getSickLeave() {
        return sickLeave;
    }

    public void setSickLeave(Integer sickLeave) {
        this.sickLeave = sickLeave;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public Boolean isIsEmployed() {
        return isEmployed;
    }

    public void setIsEmployed(Boolean isEmployed) {
        this.isEmployed = isEmployed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmployeeDTO employeeDTO = (EmployeeDTO) o;
        if (employeeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employeeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "id=" + getId() +
            ", password='" + getPassword() + "'" +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", address='" + getAddress() + "'" +
            ", contact='" + getContact() + "'" +
            ", rank='" + getRank() + "'" +
            ", casualLeave=" + getCasualLeave() +
            ", sickLeave=" + getSickLeave() +
            ", joinDate='" + getJoinDate() + "'" +
            ", isEmployed='" + isIsEmployed() + "'" +
            "}";
    }
}
