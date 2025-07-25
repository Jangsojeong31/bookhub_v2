package com.bookhub.bookhub_back.dto.stock.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StockUpdateResponseDto {
    private Long stockId;
    private String branchName;
    private String type;
    private String bookIsbn;
    private String bookTitle;
    private Long amount; // 변경된 재고량
    private Long bookAmount; // 현재 재고량
}
