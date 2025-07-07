package com.bookhub.bookhub_back.dto.book.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BookCreateRequestDto {
    @NotBlank(message = "ISBN은 필수입니다.")
    private String isbn;
    @NotBlank(message = "도서 제목은 필수입니다.")
    private String bookTitle;
    @NotNull(message = "카테고리는 필수입니다.")
    private Long categoryId;
    @NotNull(message = "저자는 필수입니다.")
    private Long authorId;
    @NotNull(message = "출판사는 필수입니다.")
    private Long publisherId;
    @NotNull(message = "가격은 필수입니다.")
    private Long bookPrice;
    @NotNull(message = "출판일은 필수입니다.")
    private LocalDate publishedDate;
    @NotBlank(message = "페이지수는 필수입니다.")
    private String pageCount;
    @NotBlank(message = "언어는 필수입니다.")
    private String language;
    private String description;
    private String coverUrl;
    private Long policyId;
}
