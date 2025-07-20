package com.bookhub.bookhub_back.controller.statistics;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.BranchRevenueResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.MonthlyRevenueStatisticsDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.WeekdayRevenueResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.revenue.WeeklyRevenueStatisticsDto;
import com.bookhub.bookhub_back.service.statistics.RevenueStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.ADMIN +"/statistics/revenue")
@RequiredArgsConstructor
public class RevenueStatisticsController {
    private final RevenueStatisticsService revenueService;

    // 주간
    @GetMapping("/weekly")
    @Operation(summary = "매출 통계 - 주간 통계")
    public ResponseEntity<ResponseDto<List<WeeklyRevenueStatisticsDto>>> getWeeklyRevenue(
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ){
        ResponseDto<List<WeeklyRevenueStatisticsDto>> revenue = revenueService.getWeeklyRevenue(year, month);
        return ResponseEntity.status(HttpStatus.OK).body(revenue);
    }

    // 월간
    @GetMapping("/monthly")
    @Operation(summary = "매출 통계 - 월간 통계")
    public ResponseEntity<ResponseDto<List<MonthlyRevenueStatisticsDto>>> getMonthlyRevenue(
            @RequestParam int year
    ){
        ResponseDto<List<MonthlyRevenueStatisticsDto>> revenue = revenueService.getMonthlyRevenue(year);
        return ResponseEntity.status(HttpStatus.OK).body(revenue);
    }

    // 요일별
    @GetMapping("/weekday")
    @Operation(summary = "매출 통계 - 요일별 통계")
    public ResponseEntity<ResponseDto<List<WeekdayRevenueResponseDto>>> getWeekdayRevenue(
            @RequestParam int year,
            @RequestParam int quarter
    ){
        ResponseDto<List<WeekdayRevenueResponseDto>> revenue = revenueService.getWeekdayRevenue(year, quarter);
        return ResponseEntity.status(HttpStatus.OK).body(revenue);
    }

    @GetMapping("/branch")
    @Operation(summary = "매출 통계 - 지점별 통계")
    public ResponseEntity<ResponseDto<List<BranchRevenueResponseDto>>> getBranchRevenue(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    ){
        ResponseDto<List<BranchRevenueResponseDto>> revenue = revenueService.getBranchRevenue(startDate,endDate);
        return ResponseEntity.status(HttpStatus.OK).body(revenue);
    }

}

