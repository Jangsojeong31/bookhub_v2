package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;
import com.bookhub.bookhub_back.common.enums.ChangeType;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeChangeLogListResponseDto;
import com.bookhub.bookhub_back.entity.EmployeeChangeLog;
import com.bookhub.bookhub_back.exception.InvalidSearchConditionException;
import com.bookhub.bookhub_back.repository.EmployeeChangeLogRepository;
import com.bookhub.bookhub_back.service.EmployeeChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeChangeLogServiceImpl implements EmployeeChangeLogService {
    private final EmployeeChangeLogRepository employeeChangeLogRepository;

    @Override
    public ResponseDto<List<EmployeeChangeLogListResponseDto>> searchEmployeeChangeLogs(String employeeName, String authorizerName, ChangeType changeType, LocalDate startUpdatedAt, LocalDate endUpdatedAt) {
        List<EmployeeChangeLog> employeeChangeLogs = null;

        if ((startUpdatedAt != null && endUpdatedAt == null) || (startUpdatedAt == null && endUpdatedAt != null)) {
            throw new InvalidSearchConditionException("시작일과 종료일을 입력해주세요.");
        }

        if (startUpdatedAt != null) {
            LocalDateTime fromDateTime = startUpdatedAt.atStartOfDay();
            LocalDateTime toDateTime = endUpdatedAt.atTime(23, 59, 59);

            employeeChangeLogs = employeeChangeLogRepository.searchEmployeeChangeLogs(employeeName, authorizerName, changeType, fromDateTime, toDateTime);
        } else {
            employeeChangeLogs = employeeChangeLogRepository.searchEmployeeChangeLogs(employeeName, authorizerName, changeType, null, null);
        }

        List<EmployeeChangeLogListResponseDto> responseDtos = employeeChangeLogs.stream()
                .map(employeeChangeLog -> EmployeeChangeLogListResponseDto.builder()
                        .logId(employeeChangeLog.getLogId())
                        .employeeNumber(employeeChangeLog.getEmployeeId().getEmployeeNumber())
                        .employeeName(employeeChangeLog.getEmployeeId().getName())
                        .changeType(employeeChangeLog.getChangeType())
                        .prePositionName(employeeChangeLog.getPreviousPositionId() != null
                                ? employeeChangeLog.getPreviousPositionId().getPositionName()
                                : null)
                        .preAuthorityName(employeeChangeLog.getPreviousAuthorityId() != null
                                ? employeeChangeLog.getPreviousAuthorityId().getAuthorityName()
                                : null)
                        .preBranchName(employeeChangeLog.getPreviousBranchId() != null
                                ? employeeChangeLog.getPreviousBranchId().getBranchName()
                                : null)
                        .authorizerNumber(employeeChangeLog.getAuthorizerId().getEmployeeNumber())
                        .authorizerName(employeeChangeLog.getAuthorizerId().getName())
                        .updatedAt(employeeChangeLog.getChangedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, responseDtos);
    }
}
