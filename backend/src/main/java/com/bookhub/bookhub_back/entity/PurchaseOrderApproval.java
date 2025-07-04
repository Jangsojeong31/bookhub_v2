package com.bookhub.bookhub_back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_order_approvals")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PurchaseOrderApproval {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_order_approval_id")
    private Long purchaseOrderApprovalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_approval_employee_id", nullable = false)
    private Employee employeeId; // 승인한 담당자

    @OneToOne
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrderId;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


}
