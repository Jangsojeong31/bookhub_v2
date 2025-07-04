package com.bookhub.bookhub_back.entity;

import com.bookhub.bookhub_back.common.enums.AlertTargetTable;
import com.bookhub.bookhub_back.common.enums.AlertType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Alert {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long alertId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employeeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type")
    private AlertType alertType;

    @Lob
    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_table", nullable = false)
    private AlertTargetTable alertTargetTable;

    @Column(name = "target_pk")
    private Long targetPk;

    @Column(name = "target_isbn")
    private String targetIsbn;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
