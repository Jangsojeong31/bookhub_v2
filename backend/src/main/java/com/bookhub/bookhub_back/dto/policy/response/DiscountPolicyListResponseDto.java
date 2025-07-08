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
public class DiscountPolicyListResponseDto {
    private Long policyId;
    private String policyTitle;
    private PolicyType policyType;
    private LocalDate startDate;
    private LocalDate endDate;
}
