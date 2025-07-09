package com.bookhub.bookhub_back.dto.alert.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AlertCreateRequestDto {
    private Long employeeId;
    private String alertType;
    private String message;
    private String alertTargetTable;
    private Long targetPk;
    private String targetIsbn;
}
