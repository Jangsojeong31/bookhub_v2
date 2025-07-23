package com.bookhub.bookhub_back.dto.policy.request;

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
public class DiscountPolicyUpdateRequestDto {
    private String policyDescription;
    private Integer totalPriceAchieve;
    @NotNull(message = "할인율은 필수입니다.")
    private Integer discountPercent;
    @NotNull(message = "시작일은 필수입니다.")
    private LocalDate startDate;
    private LocalDate endDate;
}
