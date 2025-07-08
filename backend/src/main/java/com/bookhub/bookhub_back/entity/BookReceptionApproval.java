package com.bookhub.bookhub_back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_reception_approvals")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class BookReceptionApproval {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_reception_approval_id")
    private Long bookReceptionApprovalId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reception_employee_id")
    private Employee receptionEmployeeId;

    @Column(name = "book_isbn", nullable = false)
    private String bookIsbn;

    @Column(name = "book_title", nullable = false)
    private String bookTitle;

    @Column(name = "purchase_order_amount", nullable = false)
    private int purchaseOrderAmount;

    @Column(name = "branch_name", nullable = false)
    private String branchName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_approval_id", nullable = false)
    private PurchaseOrderApproval purchaseOrderApprovalId;

    @Column(name = "is_reception_approved", nullable = false)
    private Boolean isReceptionApproved = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
