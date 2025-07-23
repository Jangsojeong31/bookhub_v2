package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.BookReceptionApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReceptionApprovalRepository extends JpaRepository<BookReceptionApproval, Long> {

    // 지점 관리자의 수령 대기 리스트 조회
    @Query("""
    SELECT r FROM BookReceptionApproval r
    WHERE r.branchName = :branchName
    AND r.isReceptionApproved = false
    ORDER BY r.createdAt DESC 
    """)
    List<BookReceptionApproval> findPendingByBranchName(@Param("branchName") String branchName);

    // 수령 확인(승인) 된 것들만 조회
    @Query("""
    SELECT r FROM BookReceptionApproval r
    WHERE r.branchName = :branchName
    AND r.isReceptionApproved = true
    ORDER BY r.createdAt DESC
    """)
    List<BookReceptionApproval> findConfirmedByBranchName(@Param("branchName")String branchName);

    // ADMIN 전체(모든 지점의) 수령 확인 로그 조회(branchName 혹은 bookIsbn으로 필터링 가능)
    @Query("""
    SELECT r FROM BookReceptionApproval r
    WHERE r.isReceptionApproved = true
    AND (:branchName IS NULL OR r.branchName LIKE %:branchName%)
    AND (:isbn IS NULL OR r.bookIsbn LIKE %:isbn%)
    ORDER BY r.createdAt DESC
    """)
    List<BookReceptionApproval> findConfirmedLogsByConditions(@Param("branchName")String branchName, @Param("isbn")String isbn);
}
