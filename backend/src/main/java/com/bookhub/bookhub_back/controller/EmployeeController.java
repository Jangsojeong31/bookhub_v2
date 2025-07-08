package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.common.enums.EmployeeStatus;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.employee.request.EmployeeOrganizationUpdateRequestDto;
import com.bookhub.bookhub_back.dto.employee.request.EmployeeSignUpApprovalRequestDto;
import com.bookhub.bookhub_back.dto.employee.request.EmployeeStatusUpdateRequestDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeListResponseDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeResponseDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeSignUpApprovalsResponseDto;
import com.bookhub.bookhub_back.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.ADMIN + "/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    // 조건별 조회
    @GetMapping
    public ResponseEntity<ResponseDto<List<EmployeeListResponseDto>>> searchEmployee(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String branchName,
            @RequestParam(required = false) String positionName,
            @RequestParam(required = false) String authorityName,
            @RequestParam(required = false) EmployeeStatus status
    ) {
        ResponseDto<List<EmployeeListResponseDto>> responseDto = employeeService.searchEmployee(name, branchName, positionName, authorityName, status);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

    // 회원 가입 신청 직원 조회
    @GetMapping("/pending")
    public ResponseEntity<ResponseDto<List<EmployeeSignUpApprovalsResponseDto>>> getPendingEmployee() {
        ResponseDto<List<EmployeeSignUpApprovalsResponseDto>> responseDto = employeeService.getPendingEmployee();
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

    // 직원 상세 조회
    @GetMapping("/{employeeId}")
    public ResponseEntity<ResponseDto<EmployeeResponseDto>> getEmployeeById(@PathVariable Long employeeId) {
        ResponseDto<EmployeeResponseDto> responseDto = employeeService.getEmployeeById(employeeId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

    // 회원가입 승인
    @PutMapping("/{employeeId}/approve")
    public ResponseEntity<ResponseDto<EmployeeSignUpApprovalsResponseDto>> updateApproval(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeSignUpApprovalRequestDto dto,
            @AuthenticationPrincipal String loginId
    ) {
        ResponseDto<EmployeeSignUpApprovalsResponseDto> responseDto = employeeService.updateApproval(employeeId, dto, loginId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

    // 직원 정보 수정
    @PutMapping("/{employeeId}/organization")
    public ResponseEntity<ResponseDto<Void>> updateOrganization(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeOrganizationUpdateRequestDto dto,
            @AuthenticationPrincipal String loginId
    ) {
        ResponseDto<Void> responseDto = employeeService.updateOrganization(employeeId, dto, loginId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

    // 직원 상태 변경 (퇴사 처리)
    @PutMapping("/{employeeId}/status")
    public ResponseEntity<ResponseDto<Void>> updateStatus(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeStatusUpdateRequestDto dto,
            @AuthenticationPrincipal String LoginId
    ) {
        ResponseDto<Void> responseDto = employeeService.updateStatus(employeeId, dto, LoginId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

}
