package com.bookhub.bookhub_back.service;


import com.bookhub.bookhub_back.common.enums.ExitReason;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeExitLogListResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeExitLogService {
    ResponseDto<List<EmployeeExitLogListResponseDto>> searchEmployeeExitLogs(
            String employeeName, String authorizerName, ExitReason exitReason, LocalDate startUpdatedAt, LocalDate endUpdatedAt);
}
