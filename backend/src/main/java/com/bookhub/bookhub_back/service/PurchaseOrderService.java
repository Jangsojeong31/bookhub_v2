package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.common.enums.PurchaseOrderStatus;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.purchaseOrder.request.PurchaseOrderApproveRequestDto;
import com.bookhub.bookhub_back.dto.purchaseOrder.request.PurchaseOrderRequestDto;
import com.bookhub.bookhub_back.dto.purchaseOrder.response.PurchaseOrderResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface PurchaseOrderService {

    ResponseDto<PurchaseOrderResponseDto> createPurchaseOrder(String loginId, @Valid PurchaseOrderRequestDto dto);

    ResponseDto<List<PurchaseOrderResponseDto>> searchPurchaseOrder(String loginId, String employeeName, String bookIsbn, PurchaseOrderStatus purchaseOrderStatus);

    ResponseDto<PurchaseOrderResponseDto> updatePurchaseOrder(@Valid PurchaseOrderRequestDto dto, Long purchaseOrderId);

    ResponseDto<Void> deletePurchaseOrder(Long purchaseOrderId);

    ResponseDto<List<PurchaseOrderResponseDto>> getAllPurchaseOrdersRequested();

    ResponseDto<PurchaseOrderResponseDto> approvePurchaseOrder(String loginId, Long purchaseOrderId, PurchaseOrderApproveRequestDto dto);
}
