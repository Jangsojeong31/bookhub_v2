package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.common.enums.ChangeType;
import com.bookhub.bookhub_back.common.enums.ExitReason;
import com.bookhub.bookhub_back.entity.EmployeeChangeLog;
import com.bookhub.bookhub_back.entity.EmployeeExitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployeeExitLogRepository extends JpaRepository<EmployeeExitLog, Long> {


    @Query("""
    SELECT e FROM EmployeeExitLog e
    WHERE (:employeeName IS NULL OR e.employeeId.name LIKE %:employeeName%)
    AND (:authorizerName IS NULL OR e.authorizerId.name LIKE %:authorizerName%)
    AND (:exitReason IS NULL OR e.exitReason = :exitReason)
    AND (
        (:startUpdatedAt IS NULL AND :endUpdatedAt IS NULL)
        OR (e.exitAt BETWEEN :startUpdatedAt AND :endUpdatedAt)
        )
    ORDER BY e.exitAt DESC 
    """)
    List<EmployeeExitLog> searchEmployeeExitLogs(
            @Param("employeeName") String employeeName,
            @Param("authorizerName") String authorizerName,
            @Param("exitReason") ExitReason exitReason,
            @Param("startUpdatedAt") LocalDateTime startUpdatedAt,
            @Param("endUpdatedAt") LocalDateTime endUpdatedAt
    );
}
