package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.common.enums.StockActionType;
import com.bookhub.bookhub_back.entity.Branch;
import com.bookhub.bookhub_back.entity.Employee;
import com.bookhub.bookhub_back.entity.StockLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockLogRepository extends JpaRepository<StockLog, Long> {

    @Query("""
            SELECT sl FROM StockLog sl
            WHERE sl.branchId = :branch
            AND (:type IS NULL OR sl.stockActionType = :type)
            AND (:bookIsbn IS NULL OR sl.bookIsbn.bookIsbn = :bookIsbn)
            AND (:start IS NULL OR sl.actionedAt >= :start)
            AND (:end IS NULL OR sl.actionedAt <= :end)
        """)
    List<StockLog> searchStockLogsByConditions(
            @Param("branch") Branch branch,
            @Param("type") StockActionType type,
            @Param("bookIsbn") String bookIsbn,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    List<StockLog> findByEmployeeId(Employee employeeId);
}
