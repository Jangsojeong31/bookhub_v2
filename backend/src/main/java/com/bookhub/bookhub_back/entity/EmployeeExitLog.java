package com.bookhub.bookhub_back.entity;

import com.bookhub.bookhub_back.common.enums.ExitReason;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_exit_logs")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class EmployeeExitLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exit_id")
    private Long exitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employeeId;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorizer_id", nullable = false)
    private Employee authorizerId;

    @CreatedDate
    @Column(name = "exit_at")
    private LocalDateTime exitAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "exit_reason", nullable = false)
    private ExitReason exitReason;

}
