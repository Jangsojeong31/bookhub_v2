package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.common.enums.ChangeType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeChangeLogListResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeChangeLogService {
    ResponseDto<List<EmployeeChangeLogListResponseDto>> searchEmployeeChangeLogs(
            String employeeName, String authorizerName, ChangeType changeType, LocalDate startUpdatedAt, LocalDate endUpdatedAt);
}
