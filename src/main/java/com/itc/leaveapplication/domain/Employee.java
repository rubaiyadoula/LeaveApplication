package com.itc.leaveapplication.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.itc.leaveapplication.domain.enumeration.EmployeeRank;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "contact")
    private String contact;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_rank")
    private EmployeeRank rank;

    @Column(name = "casual_leave")
    private Integer casualLeave;

    @Column(name = "sick_leave")
    private Integer sickLeave;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "is_employed")
    private Boolean isEmployed;

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LeaveRecord> leaveRecords = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public Employee password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public Employee name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public Employee email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public Employee address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public Employee contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public EmployeeRank getRank() {
        return rank;
    }

    public Employee rank(EmployeeRank rank) {
        this.rank = rank;
        return this;
    }

    public void setRank(EmployeeRank rank) {
        this.rank = rank;
    }

    public Integer getCasualLeave() {
        return casualLeave;
    }

    public Employee casualLeave(Integer casualLeave) {
        this.casualLeave = casualLeave;
        return this;
    }

    public void setCasualLeave(Integer casualLeave) {
        this.casualLeave = casualLeave;
    }

    public Integer getSickLeave() {
        return sickLeave;
    }

    public Employee sickLeave(Integer sickLeave) {
        this.sickLeave = sickLeave;
        return this;
    }

    public void setSickLeave(Integer sickLeave) {
        this.sickLeave = sickLeave;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public Employee joinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
        return this;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public Boolean isIsEmployed() {
        return isEmployed;
    }

    public Employee isEmployed(Boolean isEmployed) {
        this.isEmployed = isEmployed;
        return this;
    }

    public void setIsEmployed(Boolean isEmployed) {
        this.isEmployed = isEmployed;
    }

    public Set<LeaveRecord> getLeaveRecords() {
        return leaveRecords;
    }

    public Employee leaveRecords(Set<LeaveRecord> leaveRecords) {
        this.leaveRecords = leaveRecords;
        return this;
    }

    public Employee addLeaveRecord(LeaveRecord leaveRecord) {
        this.leaveRecords.add(leaveRecord);
        leaveRecord.setEmployee(this);
        return this;
    }

    public Employee removeLeaveRecord(LeaveRecord leaveRecord) {
        this.leaveRecords.remove(leaveRecord);
        leaveRecord.setEmployee(null);
        return this;
    }

    public void setLeaveRecords(Set<LeaveRecord> leaveRecords) {
        this.leaveRecords = leaveRecords;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Employee{" +
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
