package com.bookhub.bookhub_back.controller.statistics;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.BranchStockBarChartDto;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.CategoryStockResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.TimeStockChartResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.ZeroStockResponseDto;
import com.bookhub.bookhub_back.service.statistics.StocksStaticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.ADMIN + "/statistics/stocks")
@RequiredArgsConstructor
public class StocksStatisticsController {
    private final StocksStaticsService stocksStaticsService;

    //지점별 입고량 및 출고량
    @GetMapping("/branch")
    @Operation(summary = "재고 통계 - 지점별 입고량 및 출고량")
    public ResponseEntity<ResponseDto<List<BranchStockBarChartDto>>> getBranchStockBarChart(
            @RequestParam int year,
            @RequestParam int month
    ) {
        ResponseDto<List<BranchStockBarChartDto>> stockStatistics = stocksStaticsService.getBranchStockBarChart(year, month);
        return ResponseEntity.status(HttpStatus.OK).body(stockStatistics);
    }

    //시간별 입고량 추이
    @GetMapping("/period")
    @Operation(summary = "재고 통계 - 시간별 입고량 추이")
    public ResponseEntity<ResponseDto<List<TimeStockChartResponseDto>>> getTimeStockStatistics(
            @RequestParam int year
    ) {
        ResponseDto<List<TimeStockChartResponseDto>> revenue = stocksStaticsService.getTimeStockStatistics(year);
        return ResponseEntity.status(HttpStatus.OK).body(revenue);
    }

    //지점별 재고가 0인 책의 수
    @GetMapping("/zero")
    @Operation(summary = "재고 통계 - 지점별 재고가 0인 책의 수")
    public ResponseEntity<ResponseDto<List<ZeroStockResponseDto>>> getZeroStockBooks() {
        ResponseDto<List<ZeroStockResponseDto>> revenue = stocksStaticsService.getZeroStockBooks();
        return ResponseEntity.status(HttpStatus.OK).body(revenue);
    }

    //지점별 각 카테고리의 재고 비율
    @GetMapping("/category")
    @Operation(summary = "재고 통계 - 지점별 각 카테고리의 재고 비율")
    public ResponseEntity<ResponseDto<List<CategoryStockResponseDto>>> getCategoryStocks(
            @RequestParam String branchName
    ) {
        ResponseDto<List<CategoryStockResponseDto>> revenue = stocksStaticsService.getCategoryStocks(branchName);
        return ResponseEntity.status(HttpStatus.OK).body(revenue);
    }
}





























