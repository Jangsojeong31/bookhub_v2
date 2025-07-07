package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.employee.request.EmployeeSignInRequestDto;
import com.bookhub.bookhub_back.dto.employee.request.EmployeeSignUpRequestDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeSignInResponseDto;
import com.bookhub.bookhub_back.service.AuthService;
import com.bookhub.bookhub_back.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MailService mailService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<Void>> signup(
            @Valid @RequestBody EmployeeSignUpRequestDto dto
    ) {
        ResponseDto<Void> response = authService.signup(dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<EmployeeSignInResponseDto>> login(
            @Valid @RequestBody EmployeeSignInRequestDto dto
    ) {
        ResponseDto<EmployeeSignInResponseDto> response = authService.login(dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }


}