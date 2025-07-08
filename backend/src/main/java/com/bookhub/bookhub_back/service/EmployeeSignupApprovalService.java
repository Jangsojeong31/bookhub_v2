package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.common.enums.IsApproved;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeSignUpApprovalsResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeSignupApprovalService {

    ResponseDto<List<EmployeeSignUpApprovalsResponseDto>> searchSignUpApproval(
            String employeeName, IsApproved isApproved, String deniedReason, String authorizerName, LocalDate startUpdatedAt, LocalDate endUpdatedAt);
}
