package com.bookhub.bookhub_back.dto.statistics.response.revenue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WeekdayRevenueResponseDto {
    private String weekday;
    private Long totalRevenue;
}
