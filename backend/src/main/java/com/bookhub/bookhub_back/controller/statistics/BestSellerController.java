package com.bookhub.bookhub_back.controller.statistics;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.salesQuantity.BestSellerDto;
import com.bookhub.bookhub_back.service.statistics.BestSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.COMMON + "/statistics/best-seller")
@RequiredArgsConstructor
public class BestSellerController {
    private final BestSellerService bestSellerService;

    // 총합 베스트셀러
    @GetMapping
    public ResponseEntity<ResponseDto<List<BestSellerDto>>> getTop100BestSellers() {
        ResponseDto<List<BestSellerDto>> response = bestSellerService.getTop100BestSellers();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 기간별 베스트 셀러 -- 일주일, 한달, 일년
    // 일주일
    @GetMapping("/weekly")
    public ResponseEntity<ResponseDto<List<BestSellerDto>>> getWeeklyBestSellers() {
        ResponseDto<List<BestSellerDto>> response = bestSellerService.getWeeklyBestSellers();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 한달
    @GetMapping("/monthly")
    public ResponseEntity<ResponseDto<List<BestSellerDto>>> getMonthlyBestSellers() {
        ResponseDto<List<BestSellerDto>> response = bestSellerService.getMonthlyBestSellers();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 일년
    @GetMapping("/yearly")
    public ResponseEntity<ResponseDto<List<BestSellerDto>>> getYearlyBestSellers() {
        ResponseDto<List<BestSellerDto>> response = bestSellerService.getYearlyBestSellers();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 카테고리별 (일주일간)
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ResponseDto<List<BestSellerDto>>> getBestSellersByCategory(
            @PathVariable Long categoryId
    ) {
        ResponseDto<List<BestSellerDto>> response = bestSellerService.getBestSellersByCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
