package com.bookhub.bookhub_back.dto.stock.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StockListResponseDto {
    private Long stockId;
    private Long branchId;
    private String bookIsbn;
    private String branchName;
    private String bookTitle;
    private Long amount;
}
