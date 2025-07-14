package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.book.request.BookCreateRequestDto;
import com.bookhub.bookhub_back.dto.book.request.BookUpdateRequestDto;
import com.bookhub.bookhub_back.dto.book.response.BookResponseDto;
import com.bookhub.bookhub_back.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {

    ResponseDto<BookResponseDto> createBook(@Valid BookCreateRequestDto dto, UserPrincipal userPrincipal, MultipartFile coverImageFile) throws IOException;

    ResponseDto<List<BookResponseDto>> searchBook(String keyword);

    ResponseDto<BookResponseDto> updateBook(String isbn, @Valid BookUpdateRequestDto dto, UserPrincipal userPrincipal, MultipartFile coverImageFile) throws IOException;

    ResponseDto<Void> hideBook(String isbn, UserPrincipal userPrincipal);
}
