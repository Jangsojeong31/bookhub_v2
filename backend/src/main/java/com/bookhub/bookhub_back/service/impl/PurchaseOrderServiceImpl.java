package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;
import com.bookhub.bookhub_back.common.enums.AlertType;
import com.bookhub.bookhub_back.common.enums.PurchaseOrderStatus;
import com.bookhub.bookhub_back.common.util.DateUtils;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.alert.request.AlertCreateRequestDto;
import com.bookhub.bookhub_back.dto.purchaseOrder.request.PurchaseOrderApproveRequestDto;
import com.bookhub.bookhub_back.dto.purchaseOrder.request.PurchaseOrderRequestDto;
import com.bookhub.bookhub_back.dto.purchaseOrder.response.PurchaseOrderResponseDto;
import com.bookhub.bookhub_back.entity.*;
import com.bookhub.bookhub_back.repository.*;
import com.bookhub.bookhub_back.service.AlertService;
import com.bookhub.bookhub_back.service.PurchaseOrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderApprovalRepository purchaseOrderApprovalRepository;
    private final EmployeeRepository employeeRepository;
    private final BookRepository bookRepository;
    private final AlertService alertService;
    private final BookReceptionApprovalRepository bookReceptionApprovalRepository;
    private final AuthorityRepository authorityRepository;

    // 발주 요청서 작성
    @Override
    @Transactional
    public ResponseDto<PurchaseOrderResponseDto> createPurchaseOrder(String loginId, PurchaseOrderRequestDto dto) {
        Employee employee = employeeRepository.findByLoginId(loginId)
                .orElseThrow(EntityNotFoundException::new);

        Branch branch = employee.getBranchId();

        Book book = bookRepository.findById(dto.getIsbn())
                .orElseThrow(EntityNotFoundException::new);

        PurchaseOrder newOrder = PurchaseOrder.builder()
                .bookIsbn(book)
                .employeeId(employee)
                .branchId(branch)
                .purchaseOrderAmount(dto.getPurchaseOrderAmount())
                .purchaseOrderStatus(PurchaseOrderStatus.REQUESTED)
                .build();

        PurchaseOrder savedOrder = purchaseOrderRepository.save(newOrder);

        PurchaseOrderResponseDto responseDto = toResponseDto(savedOrder);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // 발주 요청서 조회
    @Override
    public ResponseDto<List<PurchaseOrderResponseDto>> searchPurchaseOrder(
            String loginId, String employeeName, String bookIsbn, PurchaseOrderStatus purchaseOrderStatus
    ) {
        Employee employee = employeeRepository.findByLoginId(loginId)
                .orElseThrow(EntityNotFoundException::new);

        Long branchId = employee.getBranchId().getBranchId();

        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findByConditions(employeeName, bookIsbn, purchaseOrderStatus, branchId);

        List<PurchaseOrderResponseDto> responseDtos = purchaseOrders.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());

        // 알림 기능
        Authority adminAuthority = authorityRepository.findByAuthorityName("ADMIN")
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessageKorean.USER_NOT_FOUND));

        for (PurchaseOrder savedOrder : purchaseOrders) {
            for (Employee admin : employeeRepository.findAll().stream()
                    .filter(emp -> emp.getAuthorityId().equals(adminAuthority))
                    .toList()) {

                alertService.createAlert(AlertCreateRequestDto.builder()
                        .employeeId(admin.getEmployeeId())
                        .alertType(String.valueOf(AlertType.PURCHASE_REQUESTED))
                        .alertTargetTable("PURCHASE_ORDERS")
                        .targetPk(savedOrder.getPurchaseOrderId())
                        .message("지점 " + savedOrder.getBranchId().getBranchName() +
                                "에서 [" + savedOrder.getBookIsbn().getBookTitle() + "] 발주 요청이 있습니다.")
                        .build());
            }
        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    // 발주 요청서 수정 - 발주량
    @Override
    public ResponseDto<PurchaseOrderResponseDto> updatePurchaseOrder(PurchaseOrderRequestDto dto, Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(EntityNotFoundException::new);

        purchaseOrder.setPurchaseOrderAmount(dto.getPurchaseOrderAmount());

        PurchaseOrder updatedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        PurchaseOrderResponseDto responseDto = toResponseDto(updatedPurchaseOrder);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // 발주 요청서 삭제
    @Override
    public ResponseDto<Void> deletePurchaseOrder(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseMessage.FAILED + purchaseOrderId));

        purchaseOrderRepository.delete(purchaseOrder);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    /* 발주 승인 페이지 기능 */
    // 발주 요청서 업데이트
    @Override
    public ResponseDto<List<PurchaseOrderResponseDto>> getAllPurchaseOrdersRequested() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findRequestedPurchaseOrders();

        List<PurchaseOrderResponseDto> responseDtos = purchaseOrders.stream()
                .map(order -> toResponseDto(order))
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    // 발주 승인
    @Override
    @Transactional
    public ResponseDto<PurchaseOrderResponseDto> approvePurchaseOrder(
            String loginId, Long purchaseOrderId, PurchaseOrderApproveRequestDto dto
    ) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(EntityNotFoundException::new);

        Employee employee = employeeRepository.findByLoginId(loginId)
                .orElseThrow(EntityNotFoundException::new);

        if(purchaseOrder.getPurchaseOrderStatus() == PurchaseOrderStatus.REQUESTED) {
                purchaseOrder.setPurchaseOrderStatus(dto.getStatus());
        } else {
            throw new IllegalArgumentException("이미 승인/승인 거절된 요청건 입니다.");
        }

        PurchaseOrder approvedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        PurchaseOrderResponseDto responseDto = toResponseDto(approvedPurchaseOrder);

        // 발주 승인 로그 생성
        PurchaseOrderApproval pOA = PurchaseOrderApproval.builder()
                .employeeId(employee)
                .purchaseOrderId(approvedPurchaseOrder)
                .isApproved(approvedPurchaseOrder.getPurchaseOrderStatus().equals(PurchaseOrderStatus.APPROVED))
                .build();

        PurchaseOrderApproval savedApproval = purchaseOrderApprovalRepository.save(pOA);

        // 알림 기능
        alertService.createAlert(AlertCreateRequestDto.builder()
                .employeeId(approvedPurchaseOrder.getEmployeeId().getEmployeeId()) // 발주 요청 담당자
                .alertType(String.valueOf(AlertType.PURCHASE_APPROVED))
                .alertTargetTable("PURCHASE_APPROVALS")
                .targetPk(approvedPurchaseOrder.getPurchaseOrderId()) // 각 발주 ID
                .message("["+approvedPurchaseOrder.getBookIsbn().getBookTitle()+"]에 대한 발주 요청이 승인되었습니다.")
                .build());

        // 수령 확인 자동 생성 (승인됐을때 자동 생성)
//        if(savedApproval.isApproved()) {
            BookReceptionApproval reception = BookReceptionApproval.builder()
                    .bookIsbn(approvedPurchaseOrder.getBookIsbn().getBookIsbn())
                    .receptionEmployeeId(null)
                    .branchName(approvedPurchaseOrder.getBranchId().getBranchName())
                    .bookTitle(approvedPurchaseOrder.getBookIsbn().getBookTitle())
                    .purchaseOrderAmount(approvedPurchaseOrder.getPurchaseOrderAmount())
                    .isReceptionApproved(false)
                    .createdAt(null)
                    .purchaseOrderApprovalId(savedApproval)
                    .build();

            bookReceptionApprovalRepository.save(reception);
//        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // responseDto 변환 메서드
    public PurchaseOrderResponseDto toResponseDto(PurchaseOrder order) {

        PurchaseOrderResponseDto responseDto = PurchaseOrderResponseDto.builder()
                .purchaseOrderId(order.getPurchaseOrderId())
                .branchName(order.getBranchId().getBranchName())
                .branchLocation(order.getBranchId().getBranchLocation())
                .employeeName(order.getEmployeeId().getName())
                .isbn(order.getBookIsbn().getBookIsbn())
                .bookTitle(order.getBookIsbn().getBookTitle())
                .bookPrice(order.getBookIsbn().getBookPrice())
                .purchaseOrderAmount(order.getPurchaseOrderAmount())
                .purchaseOrderPrice((order.getBookIsbn().getBookPrice())*(order.getPurchaseOrderAmount()))
                .purchaseOrderAt(DateUtils.format(order.getPurchaseOrderAt()))
                .purchaseOrderStatus(order.getPurchaseOrderStatus())
                .build();

        return responseDto;
    }
}
