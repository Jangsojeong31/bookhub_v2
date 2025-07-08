package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.purchaseOrderApproval.response.PurchaseOrderApprovalResponseDto;
import com.bookhub.bookhub_back.entity.PurchaseOrderApproval;
import com.bookhub.bookhub_back.repository.PurchaseOrderApprovalRepository;
import com.bookhub.bookhub_back.service.PurchaseOrderApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderApprovalServiceImpl implements PurchaseOrderApprovalService {
    private final PurchaseOrderApprovalRepository purchaseOrderApprovalRepository;

    @Override
    public ResponseDto<List<PurchaseOrderApprovalResponseDto>> searchPurchaseOrderApprovals(
            String employeeName, Boolean isApproved, LocalDate startDate, LocalDate endDate
    ) {
        LocalDateTime startDateAt = null;
        LocalDateTime endDateAt = null;

        if(startDate != null) {
            startDateAt = startDate.atStartOfDay();
        }
        if(endDate != null) {
            endDateAt = endDate.atTime(23, 59, 59);
        }

        List<PurchaseOrderApproval> approvals = purchaseOrderApprovalRepository.findByConditions(
                employeeName, isApproved, startDateAt, endDateAt);

        List<PurchaseOrderApprovalResponseDto> responseDtos = approvals.stream()
                .map(this::toResponseDto)
                .toList();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    // responseDto 변환 메서드
    public PurchaseOrderApprovalResponseDto toResponseDto(PurchaseOrderApproval purchaseOrderApproval) {
        return  PurchaseOrderApprovalResponseDto.builder()
                .purchaseOrderApprovalId(purchaseOrderApproval.getPurchaseOrderApprovalId())
                .poDetail(
                        PurchaseOrderApprovalResponseDto.PurchaseOrderDetail.builder()
                                .branchName(purchaseOrderApproval.getPurchaseOrderId().getBranchId().getBranchName())
                                .employeeName(purchaseOrderApproval.getPurchaseOrderId().getEmployeeId().getName())
                                .isbn(purchaseOrderApproval.getPurchaseOrderId().getBookIsbn().getBookIsbn())
                                .bookTitle(purchaseOrderApproval.getPurchaseOrderId().getBookIsbn().getBookTitle())
                                .bookPrice(purchaseOrderApproval.getPurchaseOrderId().getBookIsbn().getBookPrice())
                                .purchaseOrderAmount(purchaseOrderApproval.getPurchaseOrderId().getPurchaseOrderAmount())
                                .purchaseOrderStatus(purchaseOrderApproval.getPurchaseOrderId().getPurchaseOrderStatus())
                                .build()
                )
                .employeeName(purchaseOrderApproval.getEmployeeId().getName())
                .isApproved(purchaseOrderApproval.isApproved())
                .approvedDateAt(purchaseOrderApproval.getCreatedAt())
                .build();
    }
}
