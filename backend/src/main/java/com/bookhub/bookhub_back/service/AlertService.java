package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.alert.request.AlertCreateRequestDto;
import com.bookhub.bookhub_back.dto.alert.request.AlertReadRequestDto;
import com.bookhub.bookhub_back.dto.alert.response.AlertResponseDto;

import java.util.List;

public interface AlertService {
    // 알림 생성 (트랜잭션)
    ResponseDto<AlertResponseDto> createAlert(AlertCreateRequestDto dto);

    ResponseDto<List<AlertResponseDto>> getUnreadAlert(String loginId);

    ResponseDto<Void> readAlert(AlertReadRequestDto dto);
}
