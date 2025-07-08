package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.common.enums.PolicyType;
import com.bookhub.bookhub_back.dto.PageResponseDto;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.policy.request.DiscountPolicyCreateRequestDto;
import com.bookhub.bookhub_back.dto.policy.request.DiscountPolicyUpdateRequestDto;
import com.bookhub.bookhub_back.dto.policy.response.DiscountPolicyDetailResponseDto;
import com.bookhub.bookhub_back.dto.policy.response.DiscountPolicyListResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

public interface DiscountPolicyService {

    ResponseDto<PageResponseDto<DiscountPolicyListResponseDto>> getPolicies(@Min(0) int page, @Min(1) int size, String keyword, PolicyType type, LocalDate start, LocalDate end);

    ResponseDto<DiscountPolicyDetailResponseDto> getPolicyById(Long policyId);

    ResponseDto<Void> createPolicy(@Valid DiscountPolicyCreateRequestDto dto);

    ResponseDto<Void> updatePolicy(Long policyId, @Valid DiscountPolicyUpdateRequestDto dto);

    ResponseDto<Void> deletePolicy(Long policyId);
}
