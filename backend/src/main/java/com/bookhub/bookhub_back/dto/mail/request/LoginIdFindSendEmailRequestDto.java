package com.bookhub.bookhub_back.dto.mail.request;

import com.bookhub.bookhub_back.common.constants.RegexConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginIdFindSendEmailRequestDto {
    @NotBlank(message = "이메일은 필수입니다.")
    @Pattern(regexp = RegexConstants.EMAIL_REGEX, message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = RegexConstants.PHONE_NUMBER_REGEX, message = "휴대폰 번호는 010으로 시작하고 뒤에는 8자리로 이루어져야 합니다.")
    private String phoneNumber;
}
