package com.bookhub.bookhub_back.dto.mail.request;

import com.bookhub.bookhub_back.common.constants.RegexConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PasswordResetRequestDto {
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = RegexConstants.PASSWORD_REGEX,
            message = "비밀번호는 8~16자의 영어, 숫자, 특수문자 각각 하나 이상 포함되어야 합니다.")
    private String password;

    private String confirmPassword;
}
