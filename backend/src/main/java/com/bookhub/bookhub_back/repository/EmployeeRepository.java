package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.common.enums.EmployeeStatus;
import com.bookhub.bookhub_back.entity.Authority;
import com.bookhub.bookhub_back.entity.Branch;
import com.bookhub.bookhub_back.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmployeeNumber(Long employeeNumber);

    @Query("""
            SELECT e FROM Employee e
            WHERE (:name IS NULL OR e.name LIKE %:name%)
              AND (:branchName IS NULL OR e.branchId.branchName LIKE %:branchName%)
              AND (:positionName IS NULL OR e.positionId.positionName LIKE %:positionName%)
              AND (:authorityName IS NULL OR e.positionId.authority.authorityName LIKE %:authorityName%)
              AND (:status IS NULL OR e.status = :status)
        """)
    List<Employee> searchEmployees(
            @Param("name") String name,
            @Param("branchName") String branchName,
            @Param("positionName") String positionName,
            @Param("authorityName") String authorityName,
            @Param("status") EmployeeStatus status
    );

    Optional<Employee> findByEmail(String email);

    List<Employee> findAllByPositionId_AuthorityAndBranchId_BranchId(Authority managerAuthority, Long branchId);

    List<Employee> findAllByPositionId_Authority(Authority positionIdAuthority);
}
