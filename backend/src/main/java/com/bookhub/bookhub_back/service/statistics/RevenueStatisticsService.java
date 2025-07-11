package com.bookhub.bookhub_back.service.statistics;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.BranchRevenueResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.MonthlyRevenueStatisticsDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.WeekdayRevenueResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.WeeklyRevenueStatisticsDto;

import java.time.LocalDate;
import java.util.List;

public interface RevenueStatisticsService {

    ResponseDto<List<WeeklyRevenueStatisticsDto>> getWeeklyRevenue(int year, int month);

    ResponseDto<List<MonthlyRevenueStatisticsDto>> getMonthlyRevenue(int year);

    ResponseDto<List<WeekdayRevenueResponseDto>> getWeekdayRevenue(int year, int quarter);

    ResponseDto<List<BranchRevenueResponseDto>> getBranchRevenue(LocalDate startDate, LocalDate endDate);
}
