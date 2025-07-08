package com.bookhub.bookhub_back.service;


import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.reception.response.ReceptionResponseDto;

import java.util.List;

public interface BookReceptionApprovalService {

    ResponseDto<Void> approveReception(Long id, String loginId);

    ResponseDto<List<ReceptionResponseDto>> getPendingReceptions(String loginId);

    ResponseDto<List<ReceptionResponseDto>> getManagerConfirmedReceptions(String loginId);

    ResponseDto<List<ReceptionResponseDto>> getAdminConfirmedReceptions(String branchName, String isbn);
}
