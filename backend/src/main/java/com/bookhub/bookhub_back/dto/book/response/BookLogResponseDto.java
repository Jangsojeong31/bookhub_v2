package com.bookhub.bookhub_back.dto.book.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookLogResponseDto {
    private Long bookLogId;
    private String bookIsbn;
    private String bookTitle;
    private String bookLogType;
    private Long previousPrice;
    private Integer previousDiscountRate;
    private String employeeName;
    private Long policyId;
    private LocalDate changedAt;
}
