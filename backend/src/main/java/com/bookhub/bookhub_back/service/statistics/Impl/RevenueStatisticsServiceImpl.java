package com.bookhub.bookhub_back.service.statistics.Impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.BranchRevenueResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.MonthlyRevenueStatisticsDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.WeekdayRevenueResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.WeeklyRevenueStatisticsDto;
import com.bookhub.bookhub_back.mapper.RevenueStatisticsMapper;
import com.bookhub.bookhub_back.repository.statistics.RevenueStatisticsRepository;
import com.bookhub.bookhub_back.service.statistics.RevenueStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RevenueStatisticsServiceImpl implements RevenueStatisticsService {
    private final RevenueStatisticsMapper revenueStatisticsMapper;

    @Override
    public ResponseDto<List<WeeklyRevenueStatisticsDto>> getWeeklyRevenue(int year, int month) {
        List<WeeklyRevenueStatisticsDto> responseDto = revenueStatisticsMapper.findWeeklyRevenue(year, month);
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public ResponseDto<List<MonthlyRevenueStatisticsDto>> getMonthlyRevenue(int year) {
        List<MonthlyRevenueStatisticsDto> responseDto = revenueStatisticsMapper.findMonthlyRevenue(year);
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public ResponseDto<List<WeekdayRevenueResponseDto>> getWeekdayRevenue(int year, int quarter) {
        int startMonth = (quarter -1) *3+1;
        int endMonth = startMonth+2;

        List<WeekdayRevenueResponseDto> responseDtos = revenueStatisticsMapper.findRevenueGroupedByWeekday(year, startMonth, endMonth);
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<List<BranchRevenueResponseDto>> getBranchRevenue(LocalDate startDate, LocalDate endDate) {
        List<BranchRevenueResponseDto> responseDtos = revenueStatisticsMapper.findByBranchByDate(startDate, endDate);
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }
}




































































