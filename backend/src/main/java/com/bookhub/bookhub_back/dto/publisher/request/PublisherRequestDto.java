package com.bookhub.bookhub_back.dto.publisher.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PublisherRequestDto {
    @NotBlank(message = "출판사 이름은 필수입니다.")
    private String publisherName;
}
