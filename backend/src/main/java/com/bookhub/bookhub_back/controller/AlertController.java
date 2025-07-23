package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.alert.request.AlertReadRequestDto;
import com.bookhub.bookhub_back.dto.alert.response.AlertResponseDto;
import com.bookhub.bookhub_back.security.UserPrincipal;
import com.bookhub.bookhub_back.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 +ApiMappingPattern.COMMON +"/alerts")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService alertService;

    // 안읽은 알림 조회
    @GetMapping("/unread")
    @Operation(summary = "알림 조회 (안읽은 알림)")
    public ResponseEntity<ResponseDto<List<AlertResponseDto>>> getUnreadAlert(
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ) {
        ResponseDto<List<AlertResponseDto>> responseDto = alertService.getUnreadAlert(userPrincipal.getLoginId());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 알림 '읽음' 처리
    @PutMapping("/read")
    @Operation(summary = "알림 '읽음' 처리")
    public ResponseEntity<ResponseDto<Void>> readAlert(
            @RequestBody AlertReadRequestDto dto
    ) {
        ResponseDto<Void> responseDto = alertService.readAlert(dto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
