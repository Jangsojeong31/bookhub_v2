package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.author.request.AuthorRequestDto;
import com.bookhub.bookhub_back.dto.author.response.AuthorResponseDto;
import com.bookhub.bookhub_back.dto.book.request.BookCreateRequestDto;
import com.bookhub.bookhub_back.dto.book.request.BookUpdateRequestDto;
import com.bookhub.bookhub_back.dto.book.response.BookResponseDto;
import com.bookhub.bookhub_back.service.BookService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1)
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final String BOOK_ADMIN = ApiMappingPattern.ADMIN + "/books";
    private final String BOOK_COMMON = ApiMappingPattern.COMMON + "/books";

    // 도서 등록
    @PostMapping(BOOK_ADMIN)
    public ResponseEntity<ResponseDto<BookResponseDto>> createBook(
            @AuthenticationPrincipal String loginId,
            @Valid @RequestBody BookCreateRequestDto dto,
            @RequestPart(value = "coverImageFile", required = false) MultipartFile coverImageFile
    ) throws Exception {
        ResponseDto<BookResponseDto> response = bookService.createBook(dto, loginId, coverImageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 도서 통합 검색
    @GetMapping(BOOK_COMMON)
    public ResponseEntity<ResponseDto<List<BookResponseDto>>> searchBook(
            @RequestParam String keyword
    ) {
        ResponseDto<List<BookResponseDto>> response = bookService.searchBook(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 도서 수정
    @PutMapping(BOOK_ADMIN + "/{isbn}")
    public ResponseEntity<ResponseDto<BookResponseDto>> updateBook(
            @PathVariable String isbn,
            @AuthenticationPrincipal String loginId,
            @Valid @RequestBody BookUpdateRequestDto dto,
            @RequestPart(value = "coverImageFile", required = false) MultipartFile coverImageFile
    ) throws Exception {
        ResponseDto<BookResponseDto> response = bookService.updateBook(isbn, dto, loginId, coverImageFile);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 도서 hidden 처리
    @PutMapping(BOOK_ADMIN + "/{isbn}/hidden")
    public ResponseEntity<ResponseDto<Void>> hideBook(
            @PathVariable String isbn,
            @AuthenticationPrincipal String loginId
    ) {
        ResponseDto<Void> response = bookService.hideBook(isbn, loginId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
