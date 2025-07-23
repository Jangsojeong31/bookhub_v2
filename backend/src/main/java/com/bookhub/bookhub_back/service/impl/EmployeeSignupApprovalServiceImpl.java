package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;
import com.bookhub.bookhub_back.common.enums.IsApproved;
import com.bookhub.bookhub_back.common.util.DateUtils;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeSignUpApprovalsResponseDto;
import com.bookhub.bookhub_back.entity.EmployeeSignUpApproval;
import com.bookhub.bookhub_back.exception.InvalidSearchConditionException;
import com.bookhub.bookhub_back.repository.EmployeeSignUpApprovalRepository;
import com.bookhub.bookhub_back.service.EmployeeSignupApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeSignupApprovalServiceImpl implements EmployeeSignupApprovalService {
    private final EmployeeSignUpApprovalRepository employeeSignUpApprovalRepository;

    @Override
    public ResponseDto<List<EmployeeSignUpApprovalsResponseDto>> searchSignUpApproval(String employeeName, IsApproved isApproved, String deniedReason, String authorizerName, LocalDate startUpdatedAt, LocalDate endUpdatedAt) {
        List<EmployeeSignUpApproval> employeeSignUpApprovals = null;

        if ((startUpdatedAt != null && endUpdatedAt == null) || (startUpdatedAt == null && endUpdatedAt != null)) {
            throw new InvalidSearchConditionException("시작일과 종료일을 입력해주세요.");
        }

        if (startUpdatedAt != null) {
            LocalDateTime fromDateTime = startUpdatedAt.atStartOfDay();
            LocalDateTime toDateTime = endUpdatedAt.atTime(23, 59, 59);

            employeeSignUpApprovals = employeeSignUpApprovalRepository.searchSignUpApproval(
                    employeeName, isApproved, deniedReason, authorizerName, fromDateTime, toDateTime);
        } else {
            employeeSignUpApprovals = employeeSignUpApprovalRepository.searchSignUpApproval(
                    employeeName, isApproved, deniedReason, authorizerName, null, null);
        }

        List<EmployeeSignUpApprovalsResponseDto> reponseDtos = employeeSignUpApprovals.stream()
                .map(employeeSignUpApproval -> EmployeeSignUpApprovalsResponseDto.builder()
                        .approvalId(employeeSignUpApproval.getApprovalId())
                        .employeeNumber(employeeSignUpApproval.getEmployeeId().getEmployeeNumber())
                        .employeeName(employeeSignUpApproval.getEmployeeId().getName())
                        .appliedAt(DateUtils.format(employeeSignUpApproval.getAppliedAt()))
                        .isApproved(employeeSignUpApproval.getIsApproved())
                        .deniedReason(employeeSignUpApproval.getDeniedReason())
                        .authorizerNumber(employeeSignUpApproval.getAuthorizerId().getEmployeeNumber())
                        .authorizerName(employeeSignUpApproval.getAuthorizerId().getName())
                        .updatedAt(DateUtils.format(employeeSignUpApproval.getUpdatedAt()))
                        .build())
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, reponseDtos);
    }
}
