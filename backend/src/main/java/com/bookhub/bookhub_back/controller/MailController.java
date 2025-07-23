package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.mail.request.EmployeeUpdateRequestDto;
import com.bookhub.bookhub_back.dto.mail.request.LoginIdFindSendEmailRequestDto;
import com.bookhub.bookhub_back.dto.mail.request.PasswordFindSendEmailReqestDto;
import com.bookhub.bookhub_back.dto.mail.request.PasswordResetRequestDto;
import com.bookhub.bookhub_back.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.AUTH + "/emails")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    // 아이디 찾기 이메일 전송
    @PostMapping("/account-id")
    @Operation(summary = "아이디 찾기 - 이메일 전송")
    public Mono<ResponseEntity<ResponseDto<String>>> SendEmailFindId(@Valid @RequestBody LoginIdFindSendEmailRequestDto dto) {
        return mailService.sendEmailFindId(dto);
    }

    // 아이디 찾기 시 이메일 인증 (토큰 검증), 아이디 제공
    @GetMapping("/account-id/verify")
    @Operation(summary = "아이디 찾기 - 이메일 인증 및 아이디 제공")
    public Mono<ResponseEntity<ResponseDto<String>>> verifyEmailId(@RequestParam String token) {
        return mailService.verifyEmailId(token);
    }

    // 비밀번호 변경 이메일 전송
    @PostMapping("/password-reset")
    @Operation(summary = "비밀번호 변경 - 이메일 전송")
    public Mono<ResponseEntity<ResponseDto<String>>> sendEmailResetPassword (@Valid @RequestBody PasswordFindSendEmailReqestDto dto) {
        return mailService.sendEmailResetPassword(dto);
    }

    // 비밀번호 변경 시 이메일 인증 (토큰 검증)
    @GetMapping("/password-reset/verify")
    @Operation(summary = "비밀번호 변경 - 이메일 인증")
    public Mono<ResponseEntity<ResponseDto<String>>> verifyLoginIdPassword(@RequestParam String token) {
        return mailService.verifyLoginIdPassword(token);
    }

    // 비밀번호 변경
    @PutMapping("/password-reset")
    @Operation(summary = "비밀번호 변경")
    public Mono<ResponseEntity<ResponseDto<String>>> passwordChange(@RequestParam String token, @Valid @RequestBody PasswordResetRequestDto dto) {
        return mailService.passwordChange(token, dto);
    }

    // 회원 가입 승인 및 거절 시 이메일 전송
    @PostMapping("/signup-approvals/{approvalId}")
    @Operation(summary = "회원 가입 승인 및 거절 시 - 이메일 전송")
    public Mono<ResponseEntity<ResponseDto<String>>> SendEmailSignUpResult(@PathVariable Long approvalId) {
        return mailService.SendEmailSignUpResult(approvalId);
    }

    // 회원 가입 승인 및 거절 시 이메일 검증 (토큰 검증)
    @GetMapping("/signup-approvals/verify")
    @Operation(summary = "회원 가입 승인 및 거절 시 - 이메일 인증")
    public Mono<ResponseEntity<ResponseDto<String>>> verifyEmployeeUpdate(@RequestParam String token) {
        return mailService.verifyEmployeeUpdate(token);
    }

    // 거절 시 회원 정보 수정
    @PutMapping("/signup-approvals/update")
    @Operation(summary = "회원 가입 승인 및 거절 시 - 회원 정보 수정")
    public Mono<ResponseEntity<ResponseDto<String>>> employeeUpdate(@RequestParam String token, @RequestBody EmployeeUpdateRequestDto dto) {
        return mailService.employeeUpdate(token, dto);
    }
}
