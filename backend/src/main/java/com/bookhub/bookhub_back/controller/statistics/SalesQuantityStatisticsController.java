package com.bookhub.bookhub_back.controller.statistics;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.salesQuantity.CategorySalesQuantityDto;
import com.bookhub.bookhub_back.dto.statistics.response.salesQuantity.SalesQuantityStatisticsDto;
import com.bookhub.bookhub_back.service.statistics.SalesQuantityStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 판매 수량별 통계 컨트롤러
@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.ADMIN + "/statistics/sales-quantity")
@RequiredArgsConstructor
public class SalesQuantityStatisticsController {
    private final SalesQuantityStatisticsService salesQuantityStatisticsService;

    // 기간별
    // 1) Daily
    @GetMapping("/daily")
    public ResponseEntity<ResponseDto<List<SalesQuantityStatisticsDto>>> getDailySalesQuantity(
            @RequestParam("month") int month
    ) {
        ResponseDto<List<SalesQuantityStatisticsDto>> response = salesQuantityStatisticsService.getDailySalesQuantity(month);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 2) weekly
    @GetMapping("/weekly")
    public ResponseEntity<ResponseDto<List<SalesQuantityStatisticsDto>>> getWeeklySalesQuantity(
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        ResponseDto<List<SalesQuantityStatisticsDto>> response = salesQuantityStatisticsService.getWeeklySalesQuantity(year, month);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 3) monthly
    @GetMapping("/monthly")
    public ResponseEntity<ResponseDto<List<SalesQuantityStatisticsDto>>> getMonthlySalesQuantity(
            @RequestParam int year
    ) {
        ResponseDto<List<SalesQuantityStatisticsDto>> response = salesQuantityStatisticsService.getMonthlySalesQuantity(year);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 카테고리별 (일주일간)
    @GetMapping("/category")
    public ResponseEntity<ResponseDto<List<CategorySalesQuantityDto>>> getSalesQuantityByCategory() {
        ResponseDto<List<CategorySalesQuantityDto>> response = salesQuantityStatisticsService.getSalesQuantityByCategory();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 할인항목별 (일주일간)
    @GetMapping("/discount-policy")
    public ResponseEntity<ResponseDto<List<SalesQuantityStatisticsDto>>> getSalesQuantityByDiscountPolicy(
            @RequestParam int year,
            @RequestParam int quarter
    ) {
        ResponseDto<List<SalesQuantityStatisticsDto>> response = salesQuantityStatisticsService.getSalesQuantityByDiscountPolicy(year, quarter);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 지점별
    @GetMapping("/branch")
    public ResponseEntity<ResponseDto<List<SalesQuantityStatisticsDto>>> getSalesQuantityByBranch(
            @RequestParam int year,
            @RequestParam int month
    ) {
        ResponseDto<List<SalesQuantityStatisticsDto>> response = salesQuantityStatisticsService.getSalesQuantityByBranch(year, month);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
