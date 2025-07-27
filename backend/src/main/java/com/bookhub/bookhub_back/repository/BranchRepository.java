package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.Branch;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    boolean existsByBranchName(@NotBlank(message = "지점명은 필수값입니다.") String branchName);

    List<Branch> findByBranchLocationContaining(String branchLocation);

    boolean existsByBranchNameAndBranchIdNot(String branchName, Long branchId);

    Branch findByBranchName(String branchName);
}
