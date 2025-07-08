package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.common.enums.PolicyType;
import com.bookhub.bookhub_back.entity.DiscountPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DiscountPolicyRepository extends JpaRepository<DiscountPolicy, Long> {

    @Query("""
    SELECT p FROM DiscountPolicy p
     WHERE (:keyword IS NULL OR p.policyTitle LIKE CONCAT('%', :keyword, '%'))
       AND (:type    IS NULL OR p.policyType  = :type)
       AND (:start   IS NULL OR p.startDate   >= :start)
       AND (:end     IS NULL OR p.endDate     <= :end)
    ORDER BY p.policyId DESC
    """)
    Page<DiscountPolicy> findFiltered(
            @Param("keyword") String keyword,
            @Param("type")    PolicyType type,
            @Param("start")   LocalDate start,
            @Param("end")     LocalDate end,
            Pageable pageable
    );
}
