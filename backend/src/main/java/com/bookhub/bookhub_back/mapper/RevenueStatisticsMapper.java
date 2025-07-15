package com.bookhub.bookhub_back.mapper;

import com.bookhub.bookhub_back.dto.statistics.response.revenue.BranchRevenueResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.MonthlyRevenueStatisticsDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.WeekdayRevenueResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.WeeklyRevenueStatisticsDto;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface RevenueStatisticsMapper {
    List<WeeklyRevenueStatisticsDto> findWeeklyRevenue(int year, int month);

    List<MonthlyRevenueStatisticsDto> findMonthlyRevenue(int year);

    List<WeekdayRevenueResponseDto> findRevenueGroupedByWeekday(int year, int startMonth, int endMonth);

    List<BranchRevenueResponseDto> findByBranchByDate(LocalDate startDate, LocalDate endDate);
}
