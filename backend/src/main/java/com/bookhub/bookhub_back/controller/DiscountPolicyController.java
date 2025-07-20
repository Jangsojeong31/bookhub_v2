package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.common.enums.PolicyType;
import com.bookhub.bookhub_back.dto.PageResponseDto;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.policy.response.DiscountPolicyDetailResponseDto;
import com.bookhub.bookhub_back.dto.policy.response.DiscountPolicyListResponseDto;
import com.bookhub.bookhub_back.service.DiscountPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.COMMON + "/policies")
@RequiredArgsConstructor
public class DiscountPolicyController {
    private final DiscountPolicyService discountPolicyService;

    // 전체 조회
    @GetMapping
    @Operation(summary = "할인 정책 전체 조회 - common")
    public ResponseEntity<ResponseDto<PageResponseDto<DiscountPolicyListResponseDto>>> getPolicies(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) PolicyType type,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate start,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate end
    ) {
        // service 레이어에서 keyword/type/start/end 를 조합해 동적 쿼리 처리하도록 구현
        ResponseDto<PageResponseDto<DiscountPolicyListResponseDto>> response =
                discountPolicyService.getPolicies(page, size, keyword, type, start, end);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 단건 조회
    @GetMapping("/{policyId}")
    @Operation(summary = "할인 정책 단건 조회 - common")
    public ResponseEntity<ResponseDto<DiscountPolicyDetailResponseDto>> getPolicyById(
            @PathVariable Long policyId
    ){
        ResponseDto<DiscountPolicyDetailResponseDto> response = discountPolicyService.getPolicyById(policyId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}

