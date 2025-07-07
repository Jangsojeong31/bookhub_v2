package com.bookhub.bookhub_back.dto.book.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BookUpdateRequestDto {
    @NotBlank(message = "ISBN은 필수입니다.")
    private String isbn;
    @NotNull(message = "가격은 필수입니다.")
    private Long bookPrice;
    @NotNull(message = "카테고리는 필수입니다.")
    private Long categoryId;
    @NotNull(message = "도서 상태는 필수입니다.")
    private String bookStatus;
    private String coverUrl;
    private String description;
    private Long policyId;
}
