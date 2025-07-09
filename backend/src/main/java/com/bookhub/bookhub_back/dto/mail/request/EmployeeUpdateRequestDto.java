package com.bookhub.bookhub_back.dto.mail.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EmployeeUpdateRequestDto {
    private String phoneNumber;
    private LocalDate birthDate;
    private Long branchId;
}
