package com.bookhub.bookhub_back.service;


import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.author.request.AuthorRequestDto;
import com.bookhub.bookhub_back.dto.author.response.AuthorResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface AuthorService {

    ResponseDto<AuthorResponseDto> createAuthor(@Valid AuthorRequestDto dto);

    ResponseDto<List<AuthorResponseDto>> getAuthorsByName(String authorName);

    ResponseDto<AuthorResponseDto> updateAuthor(Long authorId, @Valid AuthorRequestDto dto);

    ResponseDto<Void> deleteAuthor(Long authorId);
}
