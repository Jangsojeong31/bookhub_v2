package com.bookhub.bookhub_back.entity;

import com.bookhub.bookhub_back.common.enums.RefundReason;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "refund_orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class RefundOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_order_id")
    private Long refundOrderId;

    @OneToOne
    @JoinColumn(name = "customer_order_id", nullable = false)
    private CustomerOrder customerOrderId;

    @CreatedDate
    @Column(name = "refunded_at", nullable = false, updatable = false)
    private LocalDateTime refundedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "refund_reason", nullable = false)
    private RefundReason refundReason;

}
