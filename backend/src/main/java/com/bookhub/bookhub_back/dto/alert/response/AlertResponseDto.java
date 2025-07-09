package com.bookhub.bookhub_back.dto.alert.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class AlertResponseDto {
    private Long alertId;
    private String alertType;
    private String message;
    private String alertTargetTable;
    private Long targetPk;
    private String targetIsbn;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
