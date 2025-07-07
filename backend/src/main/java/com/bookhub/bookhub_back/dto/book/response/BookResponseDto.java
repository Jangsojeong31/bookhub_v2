package com.bookhub.bookhub_back.dto.book.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDto {
    private String isbn;
    private String bookTitle;
    private Long categoryId;
    private String categoryName;
    private String authorName;
    private String publisherName;
    private Long bookPrice;
    private LocalDate publishedDate;
    private String coverUrl;
    private String pageCount;
    private String language;
    private String description;
    private String bookStatus;
    private Long policyId;
}
