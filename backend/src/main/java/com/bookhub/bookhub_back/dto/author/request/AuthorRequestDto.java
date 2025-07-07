package com.bookhub.bookhub_back.dto.author.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthorRequestDto {
    @NotBlank(message = "저자 이름은 필수입니다.")
    private String authorName;
    @NotBlank(message = "저자 이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String authorEmail;
}
