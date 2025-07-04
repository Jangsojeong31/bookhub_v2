package com.bookhub.bookhub_back.dto.statistics.response.stocks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@Setter
public class CategoryStockResponseDto {
    private String categoryName;
    private Long quantity;
}
