package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;
import com.bookhub.bookhub_back.common.enums.AlertTargetTable;
import com.bookhub.bookhub_back.common.enums.AlertType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.alert.request.AlertCreateRequestDto;
import com.bookhub.bookhub_back.dto.alert.request.AlertReadRequestDto;
import com.bookhub.bookhub_back.dto.alert.response.AlertResponseDto;
import com.bookhub.bookhub_back.entity.Alert;
import com.bookhub.bookhub_back.entity.Employee;
import com.bookhub.bookhub_back.repository.AlertRepository;
import com.bookhub.bookhub_back.repository.EmployeeRepository;
import com.bookhub.bookhub_back.service.AlertService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {
    private final AlertRepository alertRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public ResponseDto<AlertResponseDto> createAlert(AlertCreateRequestDto dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(EntityNotFoundException::new);

        Alert alert = Alert.builder()
                .employeeId(employee)
                .alertType(AlertType.valueOf(dto.getAlertType()))
                .message(dto.getMessage())
                .alertTargetTable(AlertTargetTable.valueOf(dto.getAlertTargetTable()))
                .targetPk(dto.getTargetPk())
                .targetIsbn(dto.getTargetIsbn())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        Alert newAlert = alertRepository.save(alert);

        AlertResponseDto responseDto = AlertResponseDto.builder()
                .alertId(newAlert.getAlertId())
                .alertType(newAlert.getAlertType().name())
                .message(newAlert.getMessage())
                .alertTargetTable(newAlert.getAlertTargetTable().name())
                .targetPk(newAlert.getTargetPk())
                .targetIsbn(newAlert.getTargetIsbn())
                .isRead(newAlert.getIsRead())
                .createdAt(newAlert.getCreatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, responseDto);
    }

    @Override
    public ResponseDto<List<AlertResponseDto>> getUnreadAlert(String loginId) {
        Employee employee = employeeRepository.findByLoginId(loginId)
                .orElseThrow(EntityNotFoundException::new);

        List<Alert> alerts = alertRepository.findByEmployeeId_EmployeeIdAndIsReadFalseOrderByCreatedAtDesc(employee);

        List<AlertResponseDto> result = alerts.stream()
                .map(alert -> AlertResponseDto.builder()
                        .alertId(alert.getAlertId())
                        .alertType(alert.getAlertType().name())
                        .message(alert.getMessage())
                        .alertTargetTable(alert.getAlertTargetTable().name())
                        .targetPk(alert.getTargetPk())
                        .targetIsbn(alert.getTargetIsbn())
                        .isRead(alert.getIsRead())
                        .createdAt(alert.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, result);
    }

    @Override
    public ResponseDto<Void> readAlert(AlertReadRequestDto dto) {
        List<Alert> alerts = alertRepository.findAllById(dto.getAlertIds());

        alerts.forEach(alert -> alert.setIsRead(true));

        alertRepository.saveAll(alerts);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS) ;
    }
}

