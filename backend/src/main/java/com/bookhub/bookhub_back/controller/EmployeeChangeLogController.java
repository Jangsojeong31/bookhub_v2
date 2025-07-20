package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.common.enums.ChangeType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeChangeLogListResponseDto;
import com.bookhub.bookhub_back.service.EmployeeChangeLogService;
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
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.ADMIN + "/employee-change-logs")
@RequiredArgsConstructor
public class EmployeeChangeLogController {
    private final EmployeeChangeLogService employeeChangeLogService;

    // 조건별 조회
    @GetMapping
    @Operation(summary = "직원 정보 변경 기록 조건별 조회")
    public ResponseEntity<ResponseDto<List<EmployeeChangeLogListResponseDto>>> searchEmployeeChangeLogs(
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) String authorizerName,
            @RequestParam(required = false) ChangeType changeType,
            @RequestParam(required = false) LocalDate startUpdatedAt,
            @RequestParam(required = false) LocalDate endUpdatedAt
    ) {
        ResponseDto<List<EmployeeChangeLogListResponseDto>> responseDto = employeeChangeLogService.searchEmployeeChangeLogs(
                employeeName, authorizerName, changeType, startUpdatedAt, endUpdatedAt);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }
}
