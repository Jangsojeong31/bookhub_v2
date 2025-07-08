package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.purchaseOrderApproval.response.PurchaseOrderApprovalResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseOrderApprovalService {

    ResponseDto<List<PurchaseOrderApprovalResponseDto>> searchPurchaseOrderApprovals(
            String employeeName, Boolean isApproved, LocalDate startDate, LocalDate endDate);
}
