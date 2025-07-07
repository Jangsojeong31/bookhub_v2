package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.EmployeeSignUpApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSignUpApprovalRepository extends JpaRepository<EmployeeSignUpApproval, Long> {

}
