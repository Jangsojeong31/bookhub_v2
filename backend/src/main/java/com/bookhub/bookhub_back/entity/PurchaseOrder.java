package com.bookhub.bookhub_back.entity;

import com.bookhub.bookhub_back.common.enums.PurchaseOrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PurchaseOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_order_id")
    private Long purchaseOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_isbn", nullable = false)
    private Book bookIsbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_employee_id", nullable = false)
    private Employee employeeId;

    @Column(name = "purchase_order_amount", nullable = false)
    private int purchaseOrderAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_order_status", nullable = false)
    private PurchaseOrderStatus purchaseOrderStatus = PurchaseOrderStatus.REQUESTED;

    @CreatedDate
    @Column(name = "purchase_order_at", nullable = false, updatable = false)
    private LocalDateTime purchaseOrderAt;

    @OneToOne(mappedBy = "purchaseOrderId", cascade = CascadeType.ALL)
    private PurchaseOrderApproval purchaseOrderApproval;
}
