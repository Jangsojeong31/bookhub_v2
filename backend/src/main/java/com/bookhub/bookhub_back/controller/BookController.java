package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.book.request.BookRequestDto;
import com.bookhub.bookhub_back.dto.book.response.BookResponseDto;
import com.bookhub.bookhub_back.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.AUTH + "/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    // 도서 등록
    @PostMapping
    public ResponseEntity<ResponseDto<BookResponseDto>> createBook(
            @AuthenticationPrincipal String loginId,
            @Valid @RequestBody BookRequestDto dto,
            @RequestPart(value = "coverImageFile", required = false) MultipartFile coverImageFile
    ) throws IOException {
        ResponseDto<BookResponseDto> response = bookService.createBook(dto, loginId, coverImageFile);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    // 도서 검색 (isbn)
    @GetMapping( "/{isbn}")
    public ResponseEntity<ResponseDto<BookResponseDto>> getBookByIsbn(
            @PathVariable String isbn
    ) {
        ResponseDto<BookResponseDto> response = bookService.getBookByIsbn(isbn);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
    // 도서 검색 (keyword)
    @GetMapping
    public ResponseEntity<ResponseDto<List<BookResponseDto>>> searchBooks(
            @RequestParam String keyword
    ) {
        ResponseDto<List<BookResponseDto>> response = bookService.searchBooks(keyword);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
    // 도서 수정
    @PutMapping( "/{isbn}")
    public ResponseEntity<ResponseDto<BookResponseDto>> updateBook(
            @AuthenticationPrincipal String loginId,
            @PathVariable String isbn,
            @Valid @RequestBody BookRequestDto dto,
            @RequestPart(value = "file", required = false) MultipartFile newCoverImageFile
    ) {
        ResponseDto<BookResponseDto> response = bookService.updateBook(isbn, dto, loginId, newCoverImageFile);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 도서 hidden 처리
    @PutMapping( "/{isbn}/hidden")
    public ResponseEntity<ResponseDto<Void>> hideBook(
            @AuthenticationPrincipal String loginId,
            @PathVariable String isbn
    ) {
        ResponseDto<Void> response = bookService.hideBook(isbn, loginId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}
