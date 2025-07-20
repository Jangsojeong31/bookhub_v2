package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.common.enums.ExitReason;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeExitLogListResponseDto;
import com.bookhub.bookhub_back.service.EmployeeExitLogService;
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
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.ADMIN + "/employee-exit-logs")
@RequiredArgsConstructor
public class EmployeeExitLogController {
    private final EmployeeExitLogService employeeExitLogService;

    @GetMapping
    @Operation(summary = "직원 퇴사 기록 조건별 조회")
    public ResponseEntity<ResponseDto<List<EmployeeExitLogListResponseDto>>> searchEmployeeExitLogs(
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) String authorizerName,
            @RequestParam(required = false) ExitReason exitReason,
            @RequestParam(required = false) LocalDate startUpdatedAt,
            @RequestParam(required = false) LocalDate endUpdatedAt
    ) {
        ResponseDto<List<EmployeeExitLogListResponseDto>> responseDto = employeeExitLogService.searchEmployeeExitLogs(
                employeeName, authorizerName, exitReason, startUpdatedAt, endUpdatedAt);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

}
