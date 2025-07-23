package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.common.enums.ChangeType;
import com.bookhub.bookhub_back.entity.EmployeeChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployeeChangeLogRepository extends JpaRepository<EmployeeChangeLog, Long> {

    @Query("""
    SELECT e FROM EmployeeChangeLog e
    WHERE (:employeeName IS NULL OR e.employeeId.name LIKE %:employeeName%)
    AND (:authorizerName IS NULL OR e.authorizerId.name LIKE %:authorizerName%)
    AND (:changeType IS NULL OR e.changeType = :changeType)
    AND (
        (:startUpdatedAt IS NULL AND :endUpdatedAt IS NULL)
        OR (e.changedAt BETWEEN :startUpdatedAt AND :endUpdatedAt)
        )
    ORDER BY e.changedAt DESC
    """)
    List<EmployeeChangeLog> searchEmployeeChangeLogs(
            @Param("employeeName") String employeeName,
            @Param("authorizerName") String authorizerName,
            @Param("changeType") ChangeType changeType,
            @Param("startUpdatedAt") LocalDateTime startUpdatedAt,
            @Param("endUpdatedAt") LocalDateTime endUpdatedAt
    );
}
