package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.common.enums.PurchaseOrderStatus;
import com.bookhub.bookhub_back.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("""
            SELECT p FROM PurchaseOrder p
            WHERE (:employeeName IS NULL OR p.employeeId.name LIKE CONCAT('%', :employeeName, '%'))
            AND (:bookIsbn IS NULL OR p.bookIsbn.bookIsbn = :bookIsbn)
            AND (:purchaseOrderStatus IS NULL OR p.purchaseOrderStatus = :purchaseOrderStatus)
            AND p.branchId.branchId = :branchId
            ORDER BY p.purchaseOrderAt DESC
            """)
    List<PurchaseOrder> findByConditions(
            @Param("employeeName") String employeeName,
            @Param("bookIsbn") String bookIsbn,
            @Param("purchaseOrderStatus") PurchaseOrderStatus purchaseOrderStatus,
            @Param("branchId") Long branchId);

    @Query("""
            SELECT p FROM PurchaseOrder p
            WHERE p.purchaseOrderStatus = "REQUESTED"
            ORDER BY p.purchaseOrderAt DESC
            """)
    List<PurchaseOrder> findRequestedPurchaseOrders();
}