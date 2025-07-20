package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.common.enums.PurchaseOrderStatus;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.purchaseOrder.request.PurchaseOrderApproveRequestDto;
import com.bookhub.bookhub_back.dto.purchaseOrder.request.PurchaseOrderRequestDto;
import com.bookhub.bookhub_back.dto.purchaseOrder.response.PurchaseOrderResponseDto;
import com.bookhub.bookhub_back.security.UserPrincipal;
import com.bookhub.bookhub_back.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1)
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    private static final String PURCHASE_ORDER_ADMIN = ApiMappingPattern.ADMIN + "/purchase-orders";
    private static final String PURCHASE_ORDER_MANAGER = ApiMappingPattern.MANAGER + "/purchase-orders";

    // 발주 요청서 작성
    @PostMapping(PURCHASE_ORDER_MANAGER)
    @Operation(summary = "발주 요청서 등록")
    public ResponseEntity<ResponseDto<PurchaseOrderResponseDto>> createPurchaseOrder(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody PurchaseOrderRequestDto dto
    ) {
        ResponseDto<PurchaseOrderResponseDto> response = purchaseOrderService.createPurchaseOrder(userPrincipal, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    // 발주 요청서 조회
    @GetMapping(PURCHASE_ORDER_MANAGER)
    @Operation(summary = "발주 요청서 조건별 조회")
    public ResponseEntity<ResponseDto<List<PurchaseOrderResponseDto>>> searchPurchaseOrder(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) String bookIsbn,
            @RequestParam(required = false) PurchaseOrderStatus purchaseOrderStatus
    ) {
        ResponseDto<List<PurchaseOrderResponseDto>> response = purchaseOrderService.searchPurchaseOrder(userPrincipal, employeeName, bookIsbn, purchaseOrderStatus);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 발주 요청서 수정 - 발주량 수정
    @PutMapping(PURCHASE_ORDER_MANAGER + "/{purchaseOrderId}")
    @Operation(summary = "발주 요청서 수정 - 발주량 수정")
    public ResponseEntity<ResponseDto<PurchaseOrderResponseDto>> updatePurchaseOrder(
            @Valid @RequestBody PurchaseOrderRequestDto dto,
            @PathVariable Long purchaseOrderId
    ) {
        ResponseDto<PurchaseOrderResponseDto> response = purchaseOrderService.updatePurchaseOrder(dto, purchaseOrderId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 발주 요청서 삭제
    @DeleteMapping(PURCHASE_ORDER_MANAGER + "/{purchaseOrderId}")
    @Operation(summary = "발주 요청서 삭제")
    public ResponseEntity<ResponseDto<Void>> deletePurchaseOrder(
            @PathVariable Long purchaseOrderId
    ) {
        ResponseDto<Void> response = purchaseOrderService.deletePurchaseOrder(purchaseOrderId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    /* 발주 승인 페이지 기능 */

    // 발주 요청서 업데이트 ('승인 상태 - 요청중' 인 발주서만 전체 조회)
    @GetMapping(PURCHASE_ORDER_ADMIN + "/requested")
    @Operation(summary = "발주 요청서 조회 ('승인 상태 - 요청중'인 발주 요청서만)")
    public ResponseEntity<ResponseDto<List<PurchaseOrderResponseDto>>> getAllPurchaseOrdersRequested() {
        ResponseDto<List<PurchaseOrderResponseDto>> response = purchaseOrderService.getAllPurchaseOrdersRequested();
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
    // 발주 요청서 수정 - 발주 승인 기능 (승인 또는 승인 거절) -> purchaseOrderApproval 생성
    @PutMapping(PURCHASE_ORDER_ADMIN + "/{purchaseOrderId}/approval")
    @Operation(summary = "발주 요청서 수정 (발주 승인 기능)")
    public ResponseEntity<ResponseDto<PurchaseOrderResponseDto>> approvePurchaseOrder(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long purchaseOrderId,
            @RequestBody PurchaseOrderApproveRequestDto dto
    ){
        ResponseDto<PurchaseOrderResponseDto> response = purchaseOrderService.approvePurchaseOrder(userPrincipal, purchaseOrderId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

}
