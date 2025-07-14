package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.common.enums.PurchaseOrderStatus;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.purchaseOrder.request.PurchaseOrderApproveRequestDto;
import com.bookhub.bookhub_back.dto.purchaseOrder.request.PurchaseOrderRequestDto;
import com.bookhub.bookhub_back.dto.purchaseOrder.response.PurchaseOrderResponseDto;
import com.bookhub.bookhub_back.security.UserPrincipal;
import jakarta.validation.Valid;

import java.util.List;

public interface PurchaseOrderService {

    ResponseDto<PurchaseOrderResponseDto> createPurchaseOrder(UserPrincipal userPrincipal, @Valid PurchaseOrderRequestDto dto);

    ResponseDto<List<PurchaseOrderResponseDto>> searchPurchaseOrder(UserPrincipal userPrincipal, String employeeName, String bookIsbn, PurchaseOrderStatus purchaseOrderStatus);

    ResponseDto<PurchaseOrderResponseDto> updatePurchaseOrder(@Valid PurchaseOrderRequestDto dto, Long purchaseOrderId);

    ResponseDto<Void> deletePurchaseOrder(Long purchaseOrderId);

    ResponseDto<List<PurchaseOrderResponseDto>> getAllPurchaseOrdersRequested();

    ResponseDto<PurchaseOrderResponseDto> approvePurchaseOrder(UserPrincipal userPrincipal, Long purchaseOrderId, PurchaseOrderApproveRequestDto dto);
}
