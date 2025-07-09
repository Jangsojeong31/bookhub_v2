package com.bookhub.bookhub_back.dto.auth.request;

import com.bookhub.bookhub_back.common.constants.RegexConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.time.LocalDate;

@Getter
//@NoArgsConstructor
//@AllArgsConstructor
public class EmployeeSignUpRequestDto {
    @NotBlank(message = "아이디는 필수입니다.")
    @Pattern(regexp = RegexConstants.LOGIN_ID_REGEX, message = "아이디는 4~12자의 영어와 숫자만 사용해야 합니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = RegexConstants.PASSWORD_REGEX,
            message = "비밀번호는 8~16자의 영어, 숫자, 특수문자 각각 하나 이상 포함되어야 합니다.")
    private String password;

    private String confirmPassword;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(regexp = RegexConstants.EMAIL_REGEX, message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = RegexConstants.PHONE_NUMBER_REGEX
            , message = "휴대폰 번호는 010으로 시작하고 뒤에는 8자리로 이루어져야 합니다.")
    private String phoneNumber;

    @NotNull(message = "생년월일은 필수입니다.")
    private LocalDate birthDate;

    @NotNull(message = "지점은 필수입니다.")
    private Long branchId;
}
