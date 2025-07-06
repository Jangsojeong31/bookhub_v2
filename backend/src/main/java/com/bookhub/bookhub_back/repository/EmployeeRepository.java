package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByLoginId(String loginId);
}
