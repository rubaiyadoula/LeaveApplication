package com.itc.leaveapplication.repository;
import com.itc.leaveapplication.domain.LeaveRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LeaveRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveRecordRepository extends JpaRepository<LeaveRecord, Long> {

}
