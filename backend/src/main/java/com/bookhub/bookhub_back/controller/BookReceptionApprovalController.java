package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.reception.response.ReceptionResponseDto;
import com.bookhub.bookhub_back.security.UserPrincipal;
import com.bookhub.bookhub_back.service.BookReceptionApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1)
@RequiredArgsConstructor
public class BookReceptionApprovalController {
    private final BookReceptionApprovalService bookReceptionApprovalService;

    private final String RECEPTION_ADMIN = ApiMappingPattern.ADMIN + "/receptions";
    private final String RECEPTION_MANAGER = ApiMappingPattern.MANAGER + "/receptions";

    // 수령 확인 (지점 관리자가 확인 버튼 누름)
    @PutMapping(RECEPTION_MANAGER + "/{id}/approve")
    public ResponseEntity<ResponseDto<Void>> approveReception(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ) {
        ResponseDto<Void> response = bookReceptionApprovalService.approveReception(id, userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 수령 대기 목록 조회(지점 관리자 전용)
    @GetMapping(RECEPTION_MANAGER + "/pending")
    public ResponseEntity<ResponseDto<List<ReceptionResponseDto>>> getPendingReceptions(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<List<ReceptionResponseDto>> response = bookReceptionApprovalService.getPendingReceptions(userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 수령 완료 목록 조회(지점 관리자)
    @GetMapping(RECEPTION_MANAGER + "/confirmed")
    public ResponseEntity<ResponseDto<List<ReceptionResponseDto>>> getManagerConfirmedReceptions(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ResponseDto<List<ReceptionResponseDto>> response = bookReceptionApprovalService.getManagerConfirmedReceptions(userPrincipal);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 전체 수령 로그 조회 (관리자)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(RECEPTION_ADMIN + "/logs")
    public ResponseEntity<ResponseDto<List<ReceptionResponseDto>>> getAdminConfirmedReceptions(
            @RequestParam(required = false) String branchName,
            @RequestParam(value = "bookIsbn", required = false) String isbn
    ) {
        ResponseDto<List<ReceptionResponseDto>> response = bookReceptionApprovalService.getAdminConfirmedReceptions(branchName, isbn);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}
