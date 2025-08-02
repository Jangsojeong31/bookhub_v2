package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.alert.response.AlertResponseDto;
import com.bookhub.bookhub_back.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.AUTH)
public class TestController {

    @GetMapping("/hello")
    public ResponseEntity<ResponseDto<String>> hello(
    ) {
        ResponseDto<String> responseDto = ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, "GitHub Action Test Success!");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
