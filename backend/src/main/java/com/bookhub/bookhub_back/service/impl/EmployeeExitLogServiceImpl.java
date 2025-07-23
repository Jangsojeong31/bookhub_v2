package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;
import com.bookhub.bookhub_back.common.enums.ExitReason;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeExitLogListResponseDto;
import com.bookhub.bookhub_back.entity.EmployeeExitLog;
import com.bookhub.bookhub_back.exception.InvalidSearchConditionException;
import com.bookhub.bookhub_back.repository.EmployeeExitLogRepository;
import com.bookhub.bookhub_back.service.EmployeeExitLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeExitLogServiceImpl implements EmployeeExitLogService {
    private final EmployeeExitLogRepository employeeExitLogRepository;

    @Override
    public ResponseDto<List<EmployeeExitLogListResponseDto>> searchEmployeeExitLogs(
            String employeeName, String authorizerName, ExitReason exitReason, LocalDate startUpdatedAt, LocalDate endUpdatedAt
    ) {
        List<EmployeeExitLog> employeeExitLogs = null;

        if ((startUpdatedAt != null && endUpdatedAt == null) || (startUpdatedAt == null && endUpdatedAt != null)) {
            throw new InvalidSearchConditionException("시작일과 종료일을 입력해주세요.");
        }

        if (startUpdatedAt != null) {
            LocalDateTime fromDateTime = startUpdatedAt.atStartOfDay();
            LocalDateTime toDateTime = endUpdatedAt.atTime(23, 59, 59);

            employeeExitLogs = employeeExitLogRepository.searchEmployeeExitLogs(employeeName, authorizerName, exitReason, fromDateTime, toDateTime);
        } else {
            employeeExitLogs = employeeExitLogRepository.searchEmployeeExitLogs(employeeName, authorizerName, exitReason, null, null);
        }

        List<EmployeeExitLogListResponseDto> responseDtos = employeeExitLogs.stream()
                .map(employeeExitLog -> EmployeeExitLogListResponseDto.builder()
                        .exitId(employeeExitLog.getExitId())
                        .employeeNumber(employeeExitLog.getEmployeeId().getEmployeeNumber())
                        .employeeName(employeeExitLog.getEmployeeId().getName())
                        .branchName(employeeExitLog.getEmployeeId().getBranchId().getBranchName())
                        .positionName(employeeExitLog.getEmployeeId().getPositionId().getPositionName())
                        .status(employeeExitLog.getEmployeeId().getStatus())
                        .exitReason(employeeExitLog.getExitReason())
                        .authorizerNumber(employeeExitLog.getAuthorizerId().getEmployeeNumber())
                        .authorizerName(employeeExitLog.getAuthorizerId().getName())
                        .updatedAt(employeeExitLog.getExitAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, responseDtos);
    }
}
