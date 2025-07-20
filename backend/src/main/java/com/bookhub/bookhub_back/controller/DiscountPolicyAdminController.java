package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.policy.request.DiscountPolicyCreateRequestDto;
import com.bookhub.bookhub_back.dto.policy.request.DiscountPolicyUpdateRequestDto;
import com.bookhub.bookhub_back.service.DiscountPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.ADMIN + "/policies")
@RequiredArgsConstructor
public class DiscountPolicyAdminController {
    private final DiscountPolicyService discountPolicyService;

    // 할인 정책 생성
    @PostMapping
    @Operation(summary = "할인 정책 생성")
    public ResponseEntity<ResponseDto<Void>> createPolicy(
            @Valid @RequestBody DiscountPolicyCreateRequestDto dto
    ){
        ResponseDto<Void> response = discountPolicyService.createPolicy(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    // 할인 정책 수정
    @PutMapping("/{policyId}")
    @Operation(summary = "할인 정책 수정")
    public ResponseEntity<ResponseDto<Void>> updatePolicy(
            @PathVariable Long policyId,
            @Valid @RequestBody DiscountPolicyUpdateRequestDto dto
    ){
        ResponseDto<Void> response = discountPolicyService.updatePolicy(policyId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 할인 정책 삭제
    @Operation(summary = "할인 정책 삭제")
    @DeleteMapping("/{policyId}")
    public ResponseEntity<ResponseDto<Void>> deletePolicy(
            @PathVariable Long policyId
    ){
        ResponseDto<Void> response = discountPolicyService.deletePolicy(policyId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

}
