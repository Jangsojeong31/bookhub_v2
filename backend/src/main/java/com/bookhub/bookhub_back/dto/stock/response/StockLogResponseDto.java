package com.bookhub.bookhub_back.dto.stock.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockLogResponseDto {
    private Long stockLogId;
    private String type;
    private String employeeName;
    private String bookTitle;
    private String branchName;
    private Long amount;
    private LocalDate actionDate;
}
