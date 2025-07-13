package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;
import com.bookhub.bookhub_back.common.enums.AlertType;
import com.bookhub.bookhub_back.common.enums.EmployeeStatus;
import com.bookhub.bookhub_back.common.enums.IsApproved;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.alert.request.AlertCreateRequestDto;
import com.bookhub.bookhub_back.dto.auth.request.EmployeeSignInRequestDto;
import com.bookhub.bookhub_back.dto.auth.request.EmployeeSignUpRequestDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeResponseDto;
import com.bookhub.bookhub_back.dto.auth.response.EmployeeSignInResponseDto;
import com.bookhub.bookhub_back.entity.*;
import com.bookhub.bookhub_back.security.JwtProvider;
import com.bookhub.bookhub_back.repository.*;
import com.bookhub.bookhub_back.service.AlertService;
import com.bookhub.bookhub_back.service.AuthService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final PositionRepository positionRepository;
    private final AuthorityRepository authorityRepository;
    private final EmployeeSignUpApprovalRepository employeeSignupApprovalRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final AlertService alertService;

    // 회원가입
    @Override
    public ResponseDto<Void> signup(EmployeeSignUpRequestDto dto) {
        String loginId = dto.getLoginId();
        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();
        String email = dto.getEmail();
        String phoneNumber = dto.getPhoneNumber();

        // 아이디 중복 확인
        if (employeeRepository.existsByLoginId(loginId)) {
            return ResponseDto.fail(ResponseCode.DUPLICATED_USER_ID, ResponseMessageKorean.DUPLICATED_USER_ID);
        }

        // 비밀번호 일치 확인
        if (!password.equals(confirmPassword)) {
            return ResponseDto.fail(ResponseCode.NOT_MATCH_PASSWORD, ResponseMessageKorean.NOT_MATCH_PASSWORD);
        }

        // 이메일 중복 확인
        if (employeeRepository.existsByEmail(email)) {
            return ResponseDto.fail(ResponseCode.DUPLICATED_EMAIL, ResponseMessageKorean.DUPLICATED_EMAIL);
        }

        // 전화번호 중복 확인
        if (employeeRepository.existsByPhoneNumber(phoneNumber)) {
            return ResponseDto.fail(ResponseCode.DUPLICATED_TEL_NUMBER, ResponseMessageKorean.DUPLICATED_TEL_NUMBER);
        }

        // 비밀번호 암호화
        String encodePassword = bCryptPasswordEncoder.encode(password);

        Position position = positionRepository.findByPositionName("사원")
                .orElseGet(() -> positionRepository.save(Position.builder()
                        .positionName("사원")
                        .build()));

        Authority authority = authorityRepository.findByAuthorityName("STAFF")
                .orElseGet(() -> authorityRepository.save(Authority.builder()
                        .authorityName("STAFF")
                        .build()));

        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않은 지점입니다."));

        // 사원 번호 생성
        Random random = new Random();
        Long employeeNumber;

        do {
            int randomSixDigits = 100000 + random.nextInt(900000); // 100000~999999 범위
            String resultStr = String.format("%02d", LocalDate.now().getYear() % 100) + randomSixDigits;
            employeeNumber = Long.parseLong(resultStr);

        } while (employeeRepository.existsByEmployeeNumber(employeeNumber));

        Employee newEmployee = Employee.builder()
                .loginId(loginId)
                .password(encodePassword)
                .email(email)
                .employeeNumber(employeeNumber)
                .name(dto.getName())
                .branchId(branch)
                .positionId(position)
                .authorityId(authority)
                .phoneNumber(phoneNumber)
                .birthDate(dto.getBirthDate())
                .isApproved(IsApproved.PENDING)
                .status(EmployeeStatus.EMPLOYED)
                .build();

        employeeRepository.save(newEmployee);

        // 회원가입 승인 로그 생성
        EmployeeSignUpApproval employeeSignUpApproval = EmployeeSignUpApproval.builder()
                .employeeId(newEmployee)
                .appliedAt(newEmployee.getCreatedAt())
                .isApproved(newEmployee.getIsApproved())
                .build();

        employeeSignupApprovalRepository.save(employeeSignUpApproval);

        // 본사 관리자(ADMIN 권한)에게 알림 전송
        Authority adminAuthority = authorityRepository.findByAuthorityName("ADMIN")
                .orElseThrow(() -> new IllegalArgumentException(ResponseMessageKorean.USER_NOT_FOUND));

        final Employee finalEmployee = newEmployee;

        employeeRepository.findAll().stream()
                .filter(emp -> emp.getAuthorityId().equals(adminAuthority))
                .forEach(admin -> {
                    AlertCreateRequestDto alertDto = AlertCreateRequestDto.builder()
                            .employeeId(admin.getEmployeeId())
                            .alertType(String.valueOf(AlertType.SIGNUP_APPROVAL))
                            .alertTargetTable("EMPLOYEES")
                            .targetPk(finalEmployee.getEmployeeId())
                            .message(finalEmployee.getName() + " 님의 회원가입 승인 요청이 도착했습니다.")
                            .build();

                    alertService.createAlert(alertDto);
                });

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS);
    }

    // 로그인
    @Override
    public ResponseDto<EmployeeSignInResponseDto> login(EmployeeSignInRequestDto dto) {
        String loginId = dto.getLoginId();
        String password = dto.getPassword();

        // 아이디에 해당하는 사용자 유무 확인
        Employee employee = employeeRepository.findByLoginId(loginId)
                .orElse(null);

        if (employee == null) {
            return ResponseDto.fail(ResponseCode.NO_EXIST_USER_ID, ResponseMessageKorean.NO_EXIST_USER_ID);
        }

        // 비밀번호 일치 여부 확인
        if (!bCryptPasswordEncoder.matches(password, employee.getPassword())) {
            return ResponseDto.fail(ResponseCode.NOT_MATCH_PASSWORD, ResponseMessageKorean.NOT_MATCH_PASSWORD);
        }

        // 사원의 승인 상태가 PENDING이거나 DENIED일 경우
        if (employee.getIsApproved().equals(IsApproved.PENDING) || employee.getIsApproved().equals(IsApproved.DENIED)) {
            return ResponseDto.fail(ResponseCode.NO_PERMISSION, ResponseMessageKorean.NO_PERMISSION);
        }

        String token = jwtProvider.generateJwtToken(loginId, employee.getAuthorityId());
        int exprTime = jwtProvider.getExpiration();

        EmployeeResponseDto employeeResponseDto = EmployeeResponseDto.builder()
                .employeeId(employee.getEmployeeId())
                .employeeName(employee.getName())
                .employeeNumber(employee.getEmployeeNumber())
                .branchId(employee.getBranchId().getBranchId())
                .branchName(employee.getBranchId().getBranchName())
                .positionId(employee.getPositionId().getPositionId())
                .positionName(employee.getPositionId().getPositionName())
                .authorityId(employee.getAuthorityId().getAuthorityId())
                .authorityName(employee.getAuthorityId().getAuthorityName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .birthDate(employee.getBirthDate())
                .createdAt(employee.getCreatedAt())
                .status(employee.getStatus())
                .isApproved(employee.getIsApproved())
                .build();

        EmployeeSignInResponseDto responseDto = EmployeeSignInResponseDto.builder()
                .token(token)
                .exprTime(exprTime)
                .employee(employeeResponseDto)
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, responseDto);

    }

    // 아이디 중복 체크
    @Override
    public ResponseDto<Void> checkLoginIdDuplicate(String loginId) {
        if (employeeRepository.existsByLoginId(loginId)) {
            return ResponseDto.fail(ResponseCode.DUPLICATED_USER_ID, ResponseMessageKorean.DUPLICATED_USER_ID);
        }
        return ResponseDto.success(ResponseCode.SUCCESS, "사용 가능한 아이디입니다.");
    }

    // 로그아웃
    @Override
    public ResponseDto<Void> logout(HttpServletResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS);
    }
}