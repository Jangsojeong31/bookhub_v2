package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.authority.response.AuthorityResponseDto;
import com.bookhub.bookhub_back.service.AuthorityService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.AUTH + "/authorities")
@RequiredArgsConstructor
public class AuthorityController {
    private final AuthorityService authorityService;

    @GetMapping
    @Operation(summary = "접근 권한 전체 조회")
    public ResponseEntity<ResponseDto<List<AuthorityResponseDto>>> getAllAuthorities() {
        ResponseDto<List<AuthorityResponseDto>> responseDto = authorityService.getAllAuthorities();
        return  ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }
}
