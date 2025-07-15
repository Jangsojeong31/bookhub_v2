package com.bookhub.bookhub_back.dto.statistics.response.stocks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@Setter
public class TimeStockChartResponseDto {
    private String branchName;
    private int month;
    private Long inAmount;
    private Long lossAmount;
}
