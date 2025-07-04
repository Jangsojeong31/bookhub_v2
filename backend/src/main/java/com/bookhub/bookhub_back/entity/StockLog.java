package com.bookhub.bookhub_back.entity;

import com.bookhub.bookhub_back.common.enums.StockActionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;

@Entity
@Table(name = "stock_logs")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class StockLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private StockActionType stockActionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_isbn", nullable = false)
    private Book bookIsbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branchId;

    //변경량
    @Column(name = "amount", nullable = false)
    private Long amount;

    //누적량
    @Column(name = "book_amount", nullable = false)
    private Long bookAmount;

    @CreatedDate
    @Column(name = "actioned_at", nullable = false, updatable = false)
    private LocalDate actionedAt;

    @Lob
    @Column(name = "description")
    private String description;
}