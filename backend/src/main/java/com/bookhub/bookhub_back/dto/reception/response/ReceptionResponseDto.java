package com.bookhub.bookhub_back.dto.reception.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReceptionResponseDto {
    private Long bookReceptionApprovalId;
    private String bookIsbn;
    private String bookTitle;
    private String branchName;
    private int purchaseOrderAmount;
    private Boolean isReceptionApproved;
    private LocalDateTime receptionDateAt;
    private String receptionEmployeeName;
}
