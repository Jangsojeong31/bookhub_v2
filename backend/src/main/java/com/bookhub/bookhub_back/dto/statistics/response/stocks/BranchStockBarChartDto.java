package com.bookhub.bookhub_back.dto.statistics.response.stocks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@Setter
public class BranchStockBarChartDto {
    private String branchName;  // 지점명
    private Long inAmount;      // 입고 합계
    private Long outAmount;     // 출고 합계
    private Long lossAmount;    // 손실 합계
}
