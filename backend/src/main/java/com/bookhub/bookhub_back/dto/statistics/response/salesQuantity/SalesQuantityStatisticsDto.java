package com.bookhub.bookhub_back.dto.statistics.response.salesQuantity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesQuantityStatisticsDto {
    private Long totalSales;
    private LocalDate orderedDate;
    private Integer orderedMonth;
    private String categoryName;
    private String policyTitle;
    private String branchName;
}
