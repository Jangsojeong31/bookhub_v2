package com.bookhub.bookhub_back.dto.alert.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class AlertReadRequestDto {
    private List<Long> alertIds;
}
