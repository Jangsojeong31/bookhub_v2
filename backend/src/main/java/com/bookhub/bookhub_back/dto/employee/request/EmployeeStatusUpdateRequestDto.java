package com.bookhub.bookhub_back.dto.employee.request;

import com.bookhub.bookhub_back.common.enums.EmployeeStatus;
import com.bookhub.bookhub_back.common.enums.ExitReason;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class EmployeeStatusUpdateRequestDto {
    @NotNull
    private EmployeeStatus status;
    @NotNull(message = "퇴사 사유를 선택해주세요.")
    private ExitReason exitReason;
}
