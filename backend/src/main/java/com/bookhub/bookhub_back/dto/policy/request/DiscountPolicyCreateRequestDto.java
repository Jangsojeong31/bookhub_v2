package com.bookhub.bookhub_back.dto.policy.request;

import com.bookhub.bookhub_back.common.enums.PolicyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountPolicyCreateRequestDto {
    @NotBlank(message = "할인 정책 이름은 필수입니다.")
    private String policyTitle;
    @NotNull(message = "할인율은 필수입니다.")
    private Integer discountPercent;
    @NotNull(message = "할인 정책 타입은 필수입니다.")
    private PolicyType policyType;
    private String policyDescription;
    private Integer totalPriceAchieve;
    @NotNull(message = "시작일은 필수입니다.")
    private LocalDate startDate;
    private LocalDate endDate;
}
