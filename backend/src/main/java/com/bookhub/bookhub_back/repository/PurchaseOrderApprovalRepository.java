package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.PurchaseOrderApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderApprovalRepository extends JpaRepository <PurchaseOrderApproval, Long> {

}
