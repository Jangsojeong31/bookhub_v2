package com.bookhub.bookhub_back.dto.policy.response;

import com.bookhub.bookhub_back.common.enums.PolicyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountPolicyDetailResponseDto {
    private String policyTitle;
    private String policyDescription;
    private PolicyType policyType;
    private Integer totalPriceAchieve;
    private Integer discountPercent;
    private LocalDate startDate;
    private LocalDate endDate;
}
