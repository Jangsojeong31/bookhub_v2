package com.bookhub.bookhub_back.dto.statistics.response.salesQuantity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BestSellerDto {
    private String bookIsbn;
    private String bookTitle;
    private String authorName;
    private String categoryName;
    private String publisherName;
    private String coverUrl;
    private Long totalSales;
}
