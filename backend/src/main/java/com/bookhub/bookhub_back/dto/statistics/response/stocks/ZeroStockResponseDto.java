package com.bookhub.bookhub_back.dto.statistics.response.stocks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ZeroStockResponseDto {
    private String branchName;
    private Long zeroStockCount;
}
