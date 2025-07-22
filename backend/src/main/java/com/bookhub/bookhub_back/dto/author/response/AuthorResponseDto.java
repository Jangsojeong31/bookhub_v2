package com.bookhub.bookhub_back.dto.author.response;

import com.bookhub.bookhub_back.entity.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorResponseDto {
    private Long authorId;
    private String authorName;
    private String authorEmail;
}
