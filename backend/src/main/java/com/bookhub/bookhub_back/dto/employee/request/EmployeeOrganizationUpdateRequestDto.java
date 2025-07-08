package com.bookhub.bookhub_back.dto.employee.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class EmployeeOrganizationUpdateRequestDto {
    @NotNull(message = "지점은 필수입니다.")
    private Long branchId;
    @NotNull(message = "직급은 필수입니다.")
    private Long positionId;
    @NotNull(message = "권한은 필수입니다.")
    private Long authorityId;
}
