package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.reception.response.ReceptionResponseDto;
import com.bookhub.bookhub_back.dto.stock.request.StockUpdateRequestDto;
import com.bookhub.bookhub_back.entity.*;
import com.bookhub.bookhub_back.provider.JwtProvider;
import com.bookhub.bookhub_back.repository.*;
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
    private final BookReceptionApprovalRepository bookReceptionApprovalRepository;
    private final PurchaseOrderApprovalRepository purchaseOrderApprovalRepository;
    private final StockService stockService;
    private final AlertService alertService;
    private final AuthorityRepository authorityRepository;

    // 수령 확인
    @Override
    @Transactional
    public ResponseDto<Void> approveReception(Long id, String loginId) {
        Employee employee = employeeRepository.findByLoginId(loginId)
                .orElseThrow(EntityNotFoundException::new);

        BookReceptionApproval bookReceptionApproval = bookReceptionApprovalRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        bookReceptionApproval.setIsReceptionApproved(true);
        bookReceptionApproval.setReceptionEmployeeId(employee);
        bookReceptionApproval.setCreatedAt(LocalDateTime.now());

        // 재고 증가
        StockUpdateRequestDto stockUpdateRequestDto = StockUpdateRequestDto.builder()
                .type("IN")
                .employeeId(employee.getEmployeeId())
                .bookIsbn(bookReceptionApproval.getBookIsbn())
                .branchId(bookReceptionApproval.getPurchaseOrderApprovalId().getPurchaseOrderId().getBranchId().getBranchId())
                .amount((long) bookReceptionApproval.getPurchaseOrderAmount())
                .description("입고-수령확인")
                .build();

        stockService.updateStock(null, stockUpdateRequestDto);

        // 알림 기능: 관리자에게 수령 확인 성공 알림 보내기
//        Authority adminAuthority = authorityRepository.findByAuthorityName("ADMIN")
//                .orElseThrow(() -> new IllegalArgumentException(ResponseMessageKorean.USER_NOT_FOUND));
//
//        for (Employee admin : employeeRepository.findAll().stream()
//                .filter(emp -> emp.getAuthorityId().equals(adminAuthority))
//                .toList()) {
//
//            alertService.createAlert(AlertCreateRequestDto.builder()
//                    .employeeId(admin.getEmployeeId())
//                    .alertType("BOOK_RECEIVED_SUCCESS")
//                    .alertTargetTable("BOOK_RECEPTION_APPROVALS")
//                    .targetPk(bookReceptionApproval.getBookReceptionApprovalId())
//                    .message("지점 " + bookReceptionApproval.getBranchName() +
//                            "에서 [" + bookReceptionApproval.getBookTitle() + "] 수령 확정 되었습니다.")
//                    .build());
//        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS) ;
    }

    // 수령 대기 목록 조회(지점 관리자 전용)
    @Override
    public ResponseDto<List<ReceptionResponseDto>> getPendingReceptions(String loginId) {
        Employee employee = employeeRepository.findByLoginId(loginId)
                .orElseThrow(EntityNotFoundException::new);

        String branchName = employee.getBranchId().getBranchName();

        List<BookReceptionApproval> pendingList = bookReceptionApprovalRepository.findPendingByBranchName(branchName);

        List<ReceptionResponseDto> responseDtos = pendingList.stream()
                .map(this::toResponseDto)
                .toList();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, responseDtos);
    }

    // 수령 완료 목록 조회(지점 관리자)
    @Override
    public ResponseDto<List<ReceptionResponseDto>> getManagerConfirmedReceptions(String loginId) {
        Employee employee = employeeRepository.findByLoginId(loginId)
                .orElseThrow(EntityNotFoundException::new);

        String branchName = employee.getBranchId().getBranchName();

        List<BookReceptionApproval> confirmedList = bookReceptionApprovalRepository.findConfirmedByBranchName(branchName);

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
                .receptionEmployeeName(approval.getReceptionEmployeeId().getName() == null ? null: approval.getReceptionEmployeeId().getName())
                .build();
    }
}
