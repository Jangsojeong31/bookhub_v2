package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.auth.request.EmployeeSignInRequestDto;
import com.bookhub.bookhub_back.dto.auth.request.EmployeeSignUpRequestDto;
import com.bookhub.bookhub_back.dto.auth.response.EmployeeSignInResponseDto;
import com.bookhub.bookhub_back.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<ResponseDto<Void>> signup(
            @Valid @RequestBody EmployeeSignUpRequestDto dto
    ) {
        ResponseDto<Void> response = authService.signup(dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<ResponseDto<EmployeeSignInResponseDto>> login(
            @Valid @RequestBody EmployeeSignInRequestDto dto
    ) {
        ResponseDto<EmployeeSignInResponseDto> response = authService.login(dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 아이디 중복 확인
    @GetMapping("/users/exists/{loginId}")
    @Operation(summary = "아이디 중복 확인")
    public ResponseEntity<ResponseDto<Void>> checkLoginIdDuplicate(@PathVariable String loginId) {
        ResponseDto<Void> responseDto = authService.checkLoginIdDuplicate(loginId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

    // 로그아웃
    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<ResponseDto<Void>> logout (HttpServletResponse response) {
        ResponseDto<Void> responseDto = authService.logout(response);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }
}