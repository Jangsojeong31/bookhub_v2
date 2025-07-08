package com.bookhub.bookhub_back.dto.employee.response;

import com.bookhub.bookhub_back.common.enums.EmployeeStatus;
import com.bookhub.bookhub_back.common.enums.ExitReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class EmployeeExitLogListResponseDto {
    private Long exitId;
    private Long employeeNumber;
    private String employeeName;
    private String branchName;
    private String positionName;
    private EmployeeStatus status;
    private ExitReason exitReason;
    private Long authorizerNumber;
    private String authorizerName;
    private LocalDateTime updatedAt;
}
