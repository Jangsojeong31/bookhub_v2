package com.bookhub.bookhub_back.dto.publisher.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PublisherRequestDto {
    @NotNull(message = "출판사명을 입력해주세요.")
    private String publisherName;
}
