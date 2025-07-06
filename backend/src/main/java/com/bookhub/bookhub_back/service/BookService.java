package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.book.request.BookRequestDto;
import com.bookhub.bookhub_back.dto.book.response.BookResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {

    ResponseDto<BookResponseDto> createBook(@Valid BookRequestDto dto, String loginId, MultipartFile coverImageFile) throws IOException;

    ResponseDto<BookResponseDto> getBookByIsbn(String isbn);

    ResponseDto<List<BookResponseDto>> searchBooks(String keyword);

    ResponseDto<BookResponseDto> updateBook(String isbn, @Valid BookRequestDto dto, String loginId, MultipartFile newCoverImageFile);

    ResponseDto<Void> hideBook(String isbn, String loginId);
}
