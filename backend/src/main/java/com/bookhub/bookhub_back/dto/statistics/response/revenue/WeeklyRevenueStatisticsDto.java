package com.bookhub.bookhub_back.dto.statistics.response.revenue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyRevenueStatisticsDto {
    private LocalDate orderedDate;
    private Long totalRevenue;
}
