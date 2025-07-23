package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.common.enums.ChangeType;
import com.bookhub.bookhub_back.common.enums.IsApproved;
import com.bookhub.bookhub_back.entity.Employee;
import com.bookhub.bookhub_back.entity.EmployeeChangeLog;
import com.bookhub.bookhub_back.entity.EmployeeSignUpApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeSignUpApprovalRepository extends JpaRepository<EmployeeSignUpApproval, Long> {

    Optional<EmployeeSignUpApproval> findAllByEmployeeIdAndIsApproved(Employee employee, IsApproved isApproved);

    @Query("""
    SELECT esa FROM EmployeeSignUpApproval esa
    WHERE (:employeeName IS NULL OR esa.employeeId.name LIKE CONCAT('%', :employeeName, '%'))
              AND (:isApproved IS NULL OR esa.isApproved = :isApproved)
              AND (:deniedReason IS NULL OR (esa.deniedReason IS NOT NULL AND esa.deniedReason = :deniedReason))
              AND (:authorizerName IS NULL OR esa.authorizerId.name LIKE CONCAT('%', :authorizerName, '%'))
              AND (:startUpdatedAt IS NULL OR esa.updatedAt >= :startUpdatedAt)
              AND (:endUpdatedAt IS NULL OR esa.updatedAt <= :endUpdatedAt)
    ORDER BY esa.updatedAt DESC
    """)
    List<EmployeeSignUpApproval> searchSignUpApproval(
            @Param("employeeName") String employeeName,
            @Param("isApproved") IsApproved isApproved,
            @Param("deniedReason") String deniedReason,
            @Param("authorizerName") String authorizerName,
            @Param("startUpdatedAt") LocalDateTime startUpdatedAt,
            @Param("endUpdatedAt") LocalDateTime endUpdatedAt
    );

    List<EmployeeSignUpApproval> findAllByIsApproved(IsApproved isApproved);

    Optional<EmployeeSignUpApproval> findAllByEmployeeId_EmployeeIdAndIsApproved(Long employeeId, IsApproved isApproved);
}
