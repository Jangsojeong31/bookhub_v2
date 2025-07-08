package com.bookhub.bookhub_back.dto.employee.request;

import com.bookhub.bookhub_back.common.enums.IsApproved;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EmployeeSignUpApprovalRequestDto {

    @NotNull(message = "승인 여부를 선택해주세요.")
    private IsApproved isApproved;
    private String deniedReason;
}
