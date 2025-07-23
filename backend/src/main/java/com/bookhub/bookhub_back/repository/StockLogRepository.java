package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.common.enums.StockActionType;
import com.bookhub.bookhub_back.entity.Branch;
import com.bookhub.bookhub_back.entity.Employee;
import com.bookhub.bookhub_back.entity.StockLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockLogRepository extends JpaRepository<StockLog, Long> {

    @Query("""
            SELECT sl FROM StockLog sl
            WHERE (:branchName IS NULL OR sl.branchId.branchName LIKE CONCAT("%", :branchName, "%"))
            AND (:enumType IS NULL OR sl.stockActionType = :enumType)
            AND (:bookIsbn IS NULL OR sl.bookIsbn.bookIsbn = :bookIsbn)
            AND (:start IS NULL OR sl.actionedAt >= :start)
            AND (:end IS NULL OR sl.actionedAt <= :end)
            ORDER BY sl.actionedAt DESC 
        """)
    List<StockLog> searchStockLogsByConditions(
            @Param("branchName") String branchName,
            @Param("enumType") StockActionType enumType,
            @Param("bookIsbn") String bookIsbn,
            @Param("start") LocalDate start,
            @Param("end") LocalDate  end
    );
}
