package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.common.enums.IsApproved;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeSignUpApprovalsResponseDto;
import com.bookhub.bookhub_back.service.EmployeeSignupApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.ADMIN + "/employee-signup-approvals")
@RequiredArgsConstructor
public class EmployeeSignupApprovalController {
    private final EmployeeSignupApprovalService employeeSignupApprovalService;

    // 회원 가입 승인 로그 검색 조회
    @GetMapping
    @Operation(summary = "회원 가입 승인 기록 조건별 조회")
    public ResponseEntity<ResponseDto<List<EmployeeSignUpApprovalsResponseDto>>> searchSignUpApproval(
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) IsApproved isApproved,
            @RequestParam(required = false) String deniedReason,
            @RequestParam(required = false) String authorizerName,
            @RequestParam(required = false) LocalDate startUpdatedAt,
            @RequestParam(required = false) LocalDate endUpdatedAt
    ) {
        ResponseDto<List<EmployeeSignUpApprovalsResponseDto>> responseDto = employeeSignupApprovalService.searchSignUpApproval(
                employeeName, isApproved, deniedReason, authorizerName, startUpdatedAt, endUpdatedAt);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }
}
