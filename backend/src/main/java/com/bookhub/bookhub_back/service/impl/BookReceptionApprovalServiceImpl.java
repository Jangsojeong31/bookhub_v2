package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;

import com.bookhub.bookhub_back.common.enums.AlertType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.alert.request.AlertCreateRequestDto;
import com.bookhub.bookhub_back.dto.reception.request.ReceptionCreateRequestDto;
import com.bookhub.bookhub_back.dto.reception.response.ReceptionResponseDto;
import com.bookhub.bookhub_back.dto.stock.request.StockRequestDto;
import com.bookhub.bookhub_back.entity.*;
import com.bookhub.bookhub_back.repository.*;
import com.bookhub.bookhub_back.security.UserPrincipal;
import com.bookhub.bookhub_back.service.AlertService;
import com.bookhub.bookhub_back.service.BookReceptionApprovalService;
import com.bookhub.bookhub_back.service.StockService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookReceptionApprovalServiceImpl implements BookReceptionApprovalService {
    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final BookReceptionApprovalRepository bookReceptionApprovalRepository;
    private final PurchaseOrderApprovalRepository purchaseOrderApprovalRepository;
    private final StockService stockService;
    private final AlertService alertService;
    private final AuthorityRepository authorityRepository;
    private final StockRepository stockRepository;

    @Override
    @Transactional
    public void createReception(ReceptionCreateRequestDto dto) {
        PurchaseOrderApproval purchaseOrderApproval = purchaseOrderApprovalRepository.findById(dto.getPurchaseOrderApprovalId())
                .orElseThrow(EntityNotFoundException::new);

        BookReceptionApproval bookReceptionApproval = BookReceptionApproval.builder()
                .bookIsbn(purchaseOrderApproval.getPurchaseOrderId().getBookIsbn().getBookIsbn())
                .receptionEmployeeId(null)
                .branchName(dto.getReceivingBranch().getBranchName())
                .bookTitle(purchaseOrderApproval.getPurchaseOrderId().getBookIsbn().getBookTitle())
                .purchaseOrderAmount(purchaseOrderApproval.getPurchaseOrderId().getPurchaseOrderAmount())
                .isReceptionApproved(false)
                .createdAt(null)
                .purchaseOrderApprovalId(purchaseOrderApproval)
                .build();

        bookReceptionApprovalRepository.save(bookReceptionApproval);

        ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS);
    }

    // 수령 확인
    @Override
    @Transactional
    public ResponseDto<Void> approveReception(Long id, UserPrincipal userPrincipal) {
        Employee employee = employeeRepository.findByLoginId(userPrincipal.getLoginId())
                .orElseThrow(EntityNotFoundException::new);

        BookReceptionApproval bookReceptionApproval = bookReceptionApprovalRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        bookReceptionApproval.setIsReceptionApproved(true);
        bookReceptionApproval.setReceptionEmployeeId(employee);
        bookReceptionApproval.setCreatedAt(LocalDateTime.now());

        // 재고 생성 또는 재고량 수정
        Branch branch = branchRepository.findByBranchName(bookReceptionApproval.getBranchName());

        Stock stock = stockRepository.findByBookIsbn_BookIsbnAndBranchId_BranchId(bookReceptionApproval.getBookIsbn(), branch.getBranchId());

        StockRequestDto requestDto = StockRequestDto.builder()
                .type("IN")
                .employeeId(employee.getEmployeeId())
                .bookIsbn(bookReceptionApproval.getBookIsbn())
                .branchId(branch.getBranchId())
                .amount((long) bookReceptionApproval.getPurchaseOrderAmount())
                .description("입고-수령확인")
                .build();

        if (stock == null) {
            stockService.createStock(requestDto);
        } else {
            stockService.updateStock(stock.getStockId(), requestDto);
        }

         // 알림 기능: 관리자에게 수령 확인 성공 알림 보내기
        Authority adminAuthority = authorityRepository.findByAuthorityName("ADMIN")
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessageKorean.USER_NOT_FOUND));

        for (Employee admin : employeeRepository.findAllByPositionId_Authority(adminAuthority)) {
            alertService.createAlert(AlertCreateRequestDto.builder()
                    .employeeId(admin.getEmployeeId())
                    .alertType(String.valueOf(AlertType.BOOK_RECEIVED_SUCCESS))
                    .alertTargetTable("BOOK_RECEPTION_APPROVALS")
                    .targetPk(bookReceptionApproval.getBookReceptionApprovalId())
                    .message("지점 " + bookReceptionApproval.getBranchName() +
                            "에서 [" + bookReceptionApproval.getBookTitle() + "] 수령 확정 되었습니다.")
                    .build());
        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS) ;
    }

    // 수령 대기 목록 조회(지점 관리자 전용)
    @Override
    public ResponseDto<List<ReceptionResponseDto>> getPendingReceptions(UserPrincipal userPrincipal) {

        Branch branch = branchRepository.findById(userPrincipal.getBranchId())
                .orElseThrow(EntityNotFoundException::new);

        List<BookReceptionApproval> pendingList = bookReceptionApprovalRepository.findPendingByBranchName(branch.getBranchName());

        List<ReceptionResponseDto> responseDtos = pendingList.stream()
                .map(this::toResponseDto)
                .toList();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, responseDtos);
    }

    // 수령 완료 목록 조회(지점 관리자)
    @Override
    public ResponseDto<List<ReceptionResponseDto>> getManagerConfirmedReceptions(UserPrincipal userPrincipal) {

        Branch branch = branchRepository.findById(userPrincipal.getBranchId())
                .orElseThrow(EntityNotFoundException::new);

        List<BookReceptionApproval> confirmedList = bookReceptionApprovalRepository.findConfirmedByBranchName(branch.getBranchName());

        List<ReceptionResponseDto> responseDtos = confirmedList.stream()
                .map(this::toResponseDto)
                .toList();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, responseDtos);
    }

    // 전체 수령 로그 조회 (관리자)
    @Override
    public ResponseDto<List<ReceptionResponseDto>> getAdminConfirmedReceptions(String branchName, String isbn) {
        List<BookReceptionApproval> logs = bookReceptionApprovalRepository.findConfirmedLogsByConditions(branchName, isbn);

        List<ReceptionResponseDto> responseDtos = logs.stream()
                .map(this::toResponseDto)
                .toList();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, responseDtos);
    }

    // responseDto 변환 메서드
    private ReceptionResponseDto toResponseDto(BookReceptionApproval approval) {
        return ReceptionResponseDto.builder()
                .bookReceptionApprovalId(approval.getBookReceptionApprovalId())
                .bookIsbn(approval.getBookIsbn())
                .bookTitle(approval.getBookTitle())
                .branchName(approval.getBranchName())
                .purchaseOrderAmount(approval.getPurchaseOrderAmount())
                .isReceptionApproved(approval.getIsReceptionApproved())
                .receptionDateAt(approval.getCreatedAt())
                .receptionEmployeeName(approval.getReceptionEmployeeId() == null ? null: approval.getReceptionEmployeeId().getName())
                .build();
    }
}
