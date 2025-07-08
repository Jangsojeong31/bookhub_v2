package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.PurchaseOrderApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseOrderApprovalRepository extends JpaRepository <PurchaseOrderApproval, Long> {

    @Query("""
            SELECT p FROM PurchaseOrderApproval p
            WHERE (:employeeName IS NULL OR p.employeeId.name LIKE CONCAT( "%", :employeeName, "%"))
            AND (:isApproved IS NULL OR p.isApproved = :isApproved)
            AND (:startDateAt IS NULL OR p.createdAt >= :startDateAt)
            AND (:endDateAt IS NULL OR p.createdAt <= :endDateAt)
            ORDER BY p.createdAt DESC
            """)
    List<PurchaseOrderApproval> findByConditions(
            @Param("employeeName") String employeeName,
            @Param("isApproved") Boolean isApproved,
            @Param("startDateAt") LocalDateTime startDateAt,
            @Param("endDateAt") LocalDateTime endDateAt);
}
