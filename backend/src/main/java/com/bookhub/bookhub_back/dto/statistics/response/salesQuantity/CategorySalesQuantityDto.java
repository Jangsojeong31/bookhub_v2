package com.bookhub.bookhub_back.dto.statistics.response.salesQuantity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategorySalesQuantityDto {
    private Long totalSales;
    private String categoryType;
    private Long categoryId;
    private String categoryName;
}
