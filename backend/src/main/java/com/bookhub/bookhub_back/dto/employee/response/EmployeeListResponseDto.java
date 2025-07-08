package com.bookhub.bookhub_back.dto.employee.response;

import com.bookhub.bookhub_back.common.enums.EmployeeStatus;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeListResponseDto {
    private Long employeeId;
    private Long employeeNumber;
    private String employeeName;
    private String branchName;
    private String positionName;
    private String authorityName;
    private EmployeeStatus status;
}
