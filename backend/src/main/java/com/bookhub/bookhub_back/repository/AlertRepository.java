package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.Alert;
import com.bookhub.bookhub_back.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    @Query("""
            SELECT a FROM Alert a
            WHERE (a.employeeId = :employee)
            AND (a.isRead = false)
            ORDER BY a.createdAt DESC
        """)
    List<Alert> findByEmployeeId_EmployeeIdAndIsReadFalseOrderByCreatedAtDesc(Employee employee);
}
