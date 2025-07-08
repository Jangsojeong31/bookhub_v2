package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.EmployeeExitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeExitLogRepository extends JpaRepository<EmployeeExitLog, Long> {


}
