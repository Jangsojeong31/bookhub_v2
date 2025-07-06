package com.bookhub.bookhub_back.dto.author.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AuthorResponseDto {
    private Long authorId;
    private String authorName;
    private String authorEmail;
}
