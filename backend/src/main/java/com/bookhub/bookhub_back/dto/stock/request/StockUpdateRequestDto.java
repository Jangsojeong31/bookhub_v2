package com.bookhub.bookhub_back.dto.stock.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockUpdateRequestDto {
    private String type;
    private Long employeeId;
    private String bookIsbn;
    private Long branchId;
    private Long amount;
    private String description;
}
