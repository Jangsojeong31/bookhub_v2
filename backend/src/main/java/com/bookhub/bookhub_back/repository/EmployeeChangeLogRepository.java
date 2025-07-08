package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.EmployeeChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeChangeLogRepository extends JpaRepository<EmployeeChangeLog, Long> {

}
