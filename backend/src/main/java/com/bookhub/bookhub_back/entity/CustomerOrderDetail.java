package com.bookhub.bookhub_back.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_order_details")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CustomerOrderDetail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_order_detail_id")
    private Long customerOrderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_order_id", nullable = false)
    private CustomerOrder customerOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_isbn", nullable = false)
    private Book book;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "price", nullable = false)
    private Long price;
}
