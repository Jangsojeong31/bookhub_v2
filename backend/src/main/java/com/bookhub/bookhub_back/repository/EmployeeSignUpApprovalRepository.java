package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.common.enums.IsApproved;
import com.bookhub.bookhub_back.entity.Employee;
import com.bookhub.bookhub_back.entity.EmployeeSignUpApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeSignUpApprovalRepository extends JpaRepository<EmployeeSignUpApproval, Long> {

    Optional<EmployeeSignUpApproval> findAllByEmployeeIdAndIsApproved(Employee employee, IsApproved isApproved);
}
