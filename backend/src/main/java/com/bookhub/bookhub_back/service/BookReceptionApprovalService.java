package com.bookhub.bookhub_back.service;


import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.reception.request.ReceptionCreateRequestDto;
import com.bookhub.bookhub_back.dto.reception.response.ReceptionResponseDto;
import com.bookhub.bookhub_back.security.UserPrincipal;

import java.util.List;

public interface BookReceptionApprovalService {
    void createReception(ReceptionCreateRequestDto dto); // 발주 승인 시 생성

    ResponseDto<Void> approveReception(Long id, UserPrincipal userPrincipal);

    ResponseDto<List<ReceptionResponseDto>> getPendingReceptions(UserPrincipal userPrincipal);

    ResponseDto<List<ReceptionResponseDto>> getManagerConfirmedReceptions(UserPrincipal userPrincipal);

    ResponseDto<List<ReceptionResponseDto>> getAdminConfirmedReceptions(String branchName, String isbn);
}
