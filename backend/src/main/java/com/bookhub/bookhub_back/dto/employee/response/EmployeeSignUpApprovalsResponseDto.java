package com.bookhub.bookhub_back.dto.employee.response;

import com.bookhub.bookhub_back.common.enums.IsApproved;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSignUpApprovalsResponseDto {
    private Long approvalId;
    private Long employeeId;
    private Long employeeNumber;
    private String employeeName;
    private String branchName;
    private String email;
    private String phoneNumber;
    private Long authorizerId;
    private Long authorizerNumber;
    private String authorizerName;
    private String appliedAt;
    private IsApproved isApproved;
    private String deniedReason;
    private String updatedAt;
}
