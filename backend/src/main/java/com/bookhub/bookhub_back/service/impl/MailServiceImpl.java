package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;
import com.bookhub.bookhub_back.common.enums.IsApproved;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.mail.request.EmployeeUpdateRequestDto;
import com.bookhub.bookhub_back.dto.mail.request.LoginIdFindSendEmailRequestDto;
import com.bookhub.bookhub_back.dto.mail.request.PasswordFindSendEmailReqestDto;
import com.bookhub.bookhub_back.dto.mail.request.PasswordResetRequestDto;
import com.bookhub.bookhub_back.entity.Branch;
import com.bookhub.bookhub_back.entity.Employee;
import com.bookhub.bookhub_back.entity.EmployeeSignUpApproval;
import com.bookhub.bookhub_back.security.JwtProvider;
import com.bookhub.bookhub_back.repository.BranchRepository;
import com.bookhub.bookhub_back.repository.EmployeeRepository;
import com.bookhub.bookhub_back.repository.EmployeeSignUpApprovalRepository;
import com.bookhub.bookhub_back.service.MailService;
import io.jsonwebtoken.Claims;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final JwtProvider jwtProvider;
    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmployeeSignUpApprovalRepository employeeSignUpApprovalRepository;
    private final BranchRepository branchRepository;

    // 아이디 찾기 이메일 전송
    @Override
    public Mono<ResponseEntity<ResponseDto<String>>> sendEmailFindId(LoginIdFindSendEmailRequestDto dto) {
        return Mono.fromCallable(() -> {

            Employee employee = employeeRepository.findByEmail(dto.getEmail())
                    .orElse(null);
            if (employee == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.fail(ResponseCode.NO_EXIST_USER_EMAIL, ResponseMessageKorean.NO_EXIST_USER_EMAIL));
            }

            if (!employee.getPhoneNumber().equals(dto.getPhoneNumber())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.fail(ResponseCode.NOT_MATCH_USER_TEL, ResponseMessageKorean.NO_EXIST_USER_TEL));
            }

            String token = jwtProvider.generateEmailValidToken(dto.getEmail(), dto.getPhoneNumber(), employee.getLoginId());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(dto.getEmail());
            helper.setSubject("이메일 인증 요청");
            String htmlContent = """
                            <h2>[이메일 인증 요청]</h2>
                            <p>
                                안녕하세요,<br><br>
                                아이디 찾기 이메일 인증을 위해 아래 버튼을 클릭해 주세요.
                            </p>
                            <a href="http://localhost:5174/auth/login-id-find?token=%s">이메일 인증하기</a>
                            <p>본 이메일은 인증 목적으로 발송되었습니다. 인증을 원하지 않으시면 무시하셔도 됩니다.</p>
                """.formatted(token);

            helper.setText(htmlContent, true);

            mailSender.send(message);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, "이메일 전송 성공"));
        });
    }

    // 아이디 찾기 시 이메일 인증 (토큰 검증), 아이디 제공
    @Override
    public Mono<ResponseEntity<ResponseDto<String>>> verifyEmailId(String token) {
        return Mono.fromCallable(() -> {
            if (!jwtProvider.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.fail(ResponseCode.TOKEN_EXPIRED, ResponseMessageKorean.TOKEN_EXPIRED));
            }

            Claims claims = jwtProvider.getClaims(token);
            String email = claims.get("email", String.class);
            String phoneNumber = claims.get("phoneNumber", String.class);
            String loginId = claims.get("loginId", String.class);

            if (email == null || phoneNumber == null || loginId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.fail(ResponseCode.TOKEN_EXPIRED, ResponseMessageKorean.TOKEN_EXPIRED));
            }

            Employee employee = employeeRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("직원이 존재하지 않습니다."));

            if (!employee.getPhoneNumber().equals(phoneNumber) || !employee.getLoginId().equals(loginId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.fail(ResponseCode.NOT_MATCH_USER_INFO, ResponseMessageKorean.NOT_MATCH_USER_INFO));
            }


            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, employee.getLoginId()));
        });
    }

    // 비밀번호 변경 이메일 전송
    @Override
    public Mono<ResponseEntity<ResponseDto<String>>> sendEmailResetPassword(PasswordFindSendEmailReqestDto dto) {
        return Mono.fromCallable(() -> {
            Employee employee = employeeRepository.findByLoginId(dto.getLoginId())
                    .orElse(null);

            if (employee == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.fail(ResponseCode.NO_EXIST_USER_ID, ResponseMessageKorean.NO_EXIST_USER_ID));
            }

            if (!employee.getEmail().equals(dto.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.fail(ResponseCode.NOT_MATCH_USER_EMAIL, ResponseMessageKorean.NOT_MATCH_USER_EMAIL));
            }

            if (!employee.getPhoneNumber().equals(dto.getPhoneNumber())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.fail(ResponseCode.NOT_MATCH_USER_TEL, ResponseMessageKorean.NOT_MATCH_USER_TEL));
            }

            String token = jwtProvider.generateEmailValidToken(dto.getEmail(), dto.getPhoneNumber(), dto.getLoginId());

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(dto.getEmail());
            helper.setSubject("이메일 인증 요청");
            String htmlContent = """
                            <h2>[이메일 인증 요청]</h2>
                            <p>
                                안녕하세요,<br><br>
                                비밀번호 변경 이메일 인증을 위해 아래 버튼을 클릭해 주세요.
                            </p>
                            <a href="http://localhost:5174/auth/password-change?token=%s">이메일 인증하기</a>
                            <p>본 이메일은 인증 목적으로 발송되었습니다. 인증을 원하지 않으시면 무시하셔도 됩니다.</p>
                """.formatted(token);

            helper.setText(htmlContent, true);

            mailSender.send(message);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, "이메일 전송 성공"));

        });
    }

    // 비밀번호 변경 시 이메일 인증 (토큰 검증)
    @Override
    public Mono<ResponseEntity<ResponseDto<String>>> verifyLoginIdPassword(String token) {
        return Mono.fromCallable(() -> {
            if (!jwtProvider.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.fail(ResponseCode.TOKEN_EXPIRED, ResponseMessageKorean.TOKEN_EXPIRED));
            }

            Claims claims = jwtProvider.getClaims(token);
            String email = claims.get("email", String.class);
            String phoneNumber = claims.get("phoneNumber", String.class);
            String loginId = claims.get("loginId", String.class);

            Employee employee = employeeRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("직원이 존재하지 않습니다."));

            if (!employee.getPhoneNumber().equals(phoneNumber) || !employee.getLoginId().equals(loginId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.fail(ResponseCode.NOT_MATCH_USER_INFO, "토큰 정보와 사용자 정보가 일치하지 않습니다."));
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS));
        });
    }

    // 비밀번호 변경
    @Override
    public Mono<ResponseEntity<ResponseDto<String>>> passwordChange(String token, PasswordResetRequestDto dto) {
        return Mono.fromCallable(() -> {
            if (!jwtProvider.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.fail(ResponseCode.TOKEN_EXPIRED, ResponseMessageKorean.TOKEN_EXPIRED));
            }

            Claims claims = jwtProvider.getClaims(token);
            String email = claims.get("email", String.class);
            String loginId = claims.get("loginId", String.class);

            Employee employee = employeeRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("직원이 존재하지 않습니다."));

            if (!employee.getLoginId().equals(loginId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.fail(ResponseCode.NOT_MATCH_USER_INFO, "토큰 정보와 사용자 정보가 일치하지 않습니다."));
            }

            String password = dto.getPassword();
            String confirmPassword = dto.getConfirmPassword();

            if (!password.equals(confirmPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.fail(ResponseCode.NOT_MATCH_PASSWORD, ResponseMessageKorean.NOT_MATCH_PASSWORD));
            }

            String encodePassword = bCryptPasswordEncoder.encode(password);
            employee.setPassword(encodePassword);
            employeeRepository.save(employee);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, "비밀번호가 변경되었습니다."));
        });
    }

    // 회원가입 승인 (또는 승인 거절) 시 이메일 전송
    @Override
    public Mono<ResponseEntity<ResponseDto<String>>> SendEmailSignUpResult(Long approvalId) {
        EmployeeSignUpApproval employeeSignUpApproval = employeeSignUpApprovalRepository.findById(approvalId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원가입 승인 신청입니다."));

        Employee employee = employeeRepository.findById(employeeSignUpApproval.getEmployeeId().getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 사원입니다."));

        if (employee.getIsApproved() == IsApproved.APPROVED) {
            return Mono.fromCallable(() -> {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setTo(employee.getEmail());
                helper.setSubject("회원가입 승인 결과");
                String htmlContent = """
                                <h2>[회원가입 승인 결과]</h2>
                                <p>
                                    안녕하세요,<br><br>
                                    회원가입이 승인되었습니다.
                                </p>
                    """;
                helper.setText(htmlContent, true);

                mailSender.send(message);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, "이메일 전송 성공"));
            });
        } else if (employee.getIsApproved() == IsApproved.DENIED && employeeSignUpApproval.getDeniedReason().equals("INVALID_EMPLOYEE_INFO")) {
            return Mono.fromCallable(() -> {
                String token = jwtProvider.generateEmailValidToken(employee.getEmail(), employee.getLoginId());

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setTo(employee.getEmail());
                helper.setSubject("회원가입 승인 결과");
                String htmlContent = """
                                <h2>[회원가입 승인 결과]</h2>
                                <p>
                                    안녕하세요,<br><br>
                                    회원 가입 승인이 거절되었습니다.
                                    거절 사유: 사원 정보 불일치
                                    정보 수정를 위해 아래 버튼을 클릭해 주세요.
                                </p>
                                    <a href="http://localhost:5174/auth/sign-up/update?token=%s">이메일 인증하기</a>
                                <p>본 이메일은 인증 목적으로 발송되었습니다. 인증을 원하지 않으시면 무시하셔도 됩니다.</p>
                    """.formatted(token);

                helper.setText(htmlContent, true);

                mailSender.send(message);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, "이메일 전송 성공"));
            });
        } else {
            return Mono.fromCallable(() -> {
                String reasonLabel = switch (employeeSignUpApproval.getDeniedReason()) {
                    case "ACCOUNT_ALREADY_EXISTS" -> "이미 계정이 발급된 사원";
                    case "CONTRACT_EMPLOYEE_RESTRICTED" -> "계약직/기간제 사용 제한";
                    case "PENDING_RESIGNATION" -> "퇴사 예정자";
                    default -> "기타 사유";
                };

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setTo(employee.getEmail());
                helper.setSubject("회원가입 승인 결과");
                String htmlContent = """
                                <h2>[회원가입 승인 결과]</h2>
                                <p>
                                    안녕하세요,<br><br>
                                    회원가입이 거절되었습니다.
                                    거절 사유: %s
                                </p>
                    """.formatted(reasonLabel);
                helper.setText(htmlContent, true);

                mailSender.send(message);

                return ResponseEntity.status(HttpStatus.OK)
                        .body(ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, "이메일 전송 성공"));
            });
        }
    }

    // 회원 정보 수정 시 이메일 검증 (토큰 검증)
    @Override
    public Mono<ResponseEntity<ResponseDto<String>>> verifyEmployeeUpdate(String token) {
        return Mono.fromCallable(() -> {
            if (!jwtProvider.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.fail(ResponseCode.TOKEN_EXPIRED, ResponseMessageKorean.TOKEN_EXPIRED));
            }

            Claims claims = jwtProvider.getClaims(token);
            String email = claims.get("email", String.class);
            String loginId = claims.get("loginId", String.class);

            Employee employee = employeeRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("직원이 존재하지 않습니다."));

            if (!employee.getLoginId().equals(loginId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.fail(ResponseCode.NOT_MATCH_USER_INFO, "토큰 정보와 사용자 정보가 일치하지 않습니다."));
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS));
        });
    }

    // 거절 시 회원 정보 수정
    @Override
    public Mono<ResponseEntity<ResponseDto<String>>> employeeUpdate(String token, EmployeeUpdateRequestDto dto) {
        return Mono.fromCallable(() -> {
            if (!jwtProvider.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.fail(ResponseCode.TOKEN_EXPIRED, ResponseMessageKorean.TOKEN_EXPIRED));
            }

            Claims claims = jwtProvider.getClaims(token);
            String email = claims.get("email", String.class);
            String loginId = claims.get("loginId", String.class);

            Employee employee = employeeRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("직원이 존재하지 않습니다."));

            if (!employee.getLoginId().equals(loginId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDto.fail(ResponseCode.NOT_MATCH_USER_INFO, "토큰 정보와 사용자 정보가 일치하지 않습니다."));
            }
            String phoneNumber = dto.getPhoneNumber();
            LocalDate birthDate = dto.getBirthDate();
            Long branchId = dto.getBranchId();

            Branch branch = branchRepository.findById(branchId)
                    .orElseThrow(() -> new IllegalArgumentException("지점이 존재하지 않습니다."));

            if (employeeRepository.existsByPhoneNumber(phoneNumber)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ResponseDto.fail(ResponseCode.DUPLICATED_TEL_NUMBER, ResponseMessageKorean.DUPLICATED_TEL_NUMBER));
            }

            employee.setPhoneNumber(phoneNumber);
            employee.setBirthDate(birthDate);
            employee.setBranchId(branch);
            employee.setIsApproved(IsApproved.PENDING);

            Employee newEmployee = employeeRepository.save(employee);

            EmployeeSignUpApproval employeeSignupApproval = EmployeeSignUpApproval.builder()
                    .employeeId(newEmployee)
                    .appliedAt(newEmployee.getCreatedAt())
                    .isApproved(newEmployee.getIsApproved())
                    .build();

            employeeSignUpApprovalRepository.save(employeeSignupApproval);


            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, "회원가입 정보가 수정되었습니다."));
        });
    }
}
