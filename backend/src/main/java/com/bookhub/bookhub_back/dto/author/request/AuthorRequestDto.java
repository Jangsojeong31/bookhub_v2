package com.bookhub.bookhub_back.dto.author.request;

import com.bookhub.bookhub_back.common.constants.RegexConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class AuthorRequestDto {
    @NotBlank(message = "저자 이름을 입력해주세요.")
    private String authorName;

    @NotBlank(message = "저자 이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String authorEmail;
}
