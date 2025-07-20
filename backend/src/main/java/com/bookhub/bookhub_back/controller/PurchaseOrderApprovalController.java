package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.purchaseOrderApproval.response.PurchaseOrderApprovalResponseDto;
import com.bookhub.bookhub_back.service.PurchaseOrderApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.ADMIN + "/purchase-order-approvals")
@RequiredArgsConstructor
public class PurchaseOrderApprovalController {
    private final PurchaseOrderApprovalService purchaseOrderApprovalService;

    // 발주 승인 기록 조회
    @GetMapping
    @Operation(summary = "발주 승인 기록 조건별 조회")
    public ResponseEntity<ResponseDto<List<PurchaseOrderApprovalResponseDto>>> searchPurchaseOrderApprovals(
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) Boolean isApproved,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        ResponseDto<List<PurchaseOrderApprovalResponseDto>> response
                = purchaseOrderApprovalService.searchPurchaseOrderApprovals(employeeName, isApproved, startDate, endDate);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}
