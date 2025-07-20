package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.position.response.PositionResponseDto;
import com.bookhub.bookhub_back.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.AUTH + "/positions")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;

    // 직급 전체 조회
    @GetMapping
    @Operation(summary = "직급 전체 조회")
    public ResponseEntity<ResponseDto<List<PositionResponseDto>>> getAllPosition() {
        ResponseDto<List<PositionResponseDto>> responseDto = positionService.getAllPosition();
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }
}
