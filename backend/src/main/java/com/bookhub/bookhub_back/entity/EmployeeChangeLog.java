package com.bookhub.bookhub_back.entity;

import com.bookhub.bookhub_back.common.enums.ChangeType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_change_logs")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class EmployeeChangeLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorizer_id", nullable = false)
    private Employee authorizerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false)
    private ChangeType changeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_authority_id")
    private Authority previousAuthorityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_position_id")
    private Position previousPositionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_branch_id")
    private Branch previousBranchId;

    @CreatedDate
    @Column(name = "changed_at")
    private LocalDateTime changedAt;
}
