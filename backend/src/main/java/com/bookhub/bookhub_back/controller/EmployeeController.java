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
import com.bookhub.bookhub_back.security.UserPrincipal;
import com.bookhub.bookhub_back.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "직원 조건별 조회")
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
    @Operation(summary = "회원 가입 승인 대기 중인 직원 조회")
    public ResponseEntity<ResponseDto<List<EmployeeSignUpApprovalsResponseDto>>> getPendingEmployee() {
        ResponseDto<List<EmployeeSignUpApprovalsResponseDto>> responseDto = employeeService.getPendingEmployee();
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

    // 직원 상세 조회
    @GetMapping("/{employeeId}")
    @Operation(summary = "직원 상세 조회")
    public ResponseEntity<ResponseDto<EmployeeResponseDto>> getEmployeeById(@PathVariable Long employeeId) {
        ResponseDto<EmployeeResponseDto> responseDto = employeeService.getEmployeeById(employeeId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

    // 회원가입 승인
    @PutMapping("/{employeeId}/approve")
    @Operation(summary = "회원가입 승인")
    public ResponseEntity<ResponseDto<EmployeeSignUpApprovalsResponseDto>> updateApproval(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeSignUpApprovalRequestDto dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ) {
        ResponseDto<EmployeeSignUpApprovalsResponseDto> responseDto = employeeService.updateApproval(employeeId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

    // 직원 정보 수정 (직급, 지점, 권한)
    @PutMapping("/{employeeId}")
    @Operation(summary = "직원 정보 수정 (직급, 지점, 권한)")
    public ResponseEntity<ResponseDto<Void>> updateOrganization(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeOrganizationUpdateRequestDto dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<Void> responseDto = employeeService.updateOrganization(employeeId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

    // 직원 상태 변경 (퇴사 처리)
    @Operation(summary = "직원 상태 변경 (퇴사 처리)")
    @PutMapping("/{employeeId}/status")
    public ResponseEntity<ResponseDto<Void>> updateStatus(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeStatusUpdateRequestDto dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<Void> responseDto = employeeService.updateStatus(employeeId, dto, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

}
