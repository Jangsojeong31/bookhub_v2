package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.auth.request.EmployeeSignInRequestDto;
import com.bookhub.bookhub_back.dto.auth.request.EmployeeSignUpRequestDto;
import com.bookhub.bookhub_back.dto.auth.response.EmployeeSignInResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

public interface AuthService {

    ResponseDto<Void> signup(@Valid EmployeeSignUpRequestDto dto);

    ResponseDto<EmployeeSignInResponseDto> login(@Valid EmployeeSignInRequestDto dto);

    ResponseDto<Void> checkLoginIdDuplicate(String loginId);

    ResponseDto<Void> logout(HttpServletResponse response);
}
