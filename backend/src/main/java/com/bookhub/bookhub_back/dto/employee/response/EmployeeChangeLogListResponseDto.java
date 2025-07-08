package com.bookhub.bookhub_back.dto.employee.response;

import com.bookhub.bookhub_back.common.enums.ChangeType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class EmployeeChangeLogListResponseDto {
    private Long logId;
    private Long employeeNumber;
    private String employeeName;
    private ChangeType changeType;
    private String prePositionName;
    private String preAuthorityName;
    private String preBranchName;
    private Long authorizerNumber;
    private String authorizerName;
    private LocalDateTime updatedAt;
}
