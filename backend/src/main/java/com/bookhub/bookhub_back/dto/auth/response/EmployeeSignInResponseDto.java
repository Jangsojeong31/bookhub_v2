package com.bookhub.bookhub_back.dto.auth.response;

import com.bookhub.bookhub_back.dto.employee.response.EmployeeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class EmployeeSignInResponseDto {
    private String token;
    private int exprTime;
    private EmployeeResponseDto employee;
}
