package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.author.request.AuthorRequestDto;
import com.bookhub.bookhub_back.dto.author.response.AuthorResponseDto;
import com.bookhub.bookhub_back.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.AUTH + "/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    // 작가 등록
    @PostMapping
    public ResponseEntity<ResponseDto<AuthorResponseDto>> createAuthor(
            @Valid @RequestBody AuthorRequestDto dto
    ) {
        ResponseDto<AuthorResponseDto> response = authorService.createAuthor(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    // 작가 이름으로 조회
    @GetMapping
    public ResponseEntity<ResponseDto<List<AuthorResponseDto>>> getAuthorsByName(
            @RequestParam String authorName
    ) {
        ResponseDto<List<AuthorResponseDto>> response = authorService.getAuthorsByName(authorName);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 작가 수정
    @PutMapping("/{authorId}")
    public ResponseEntity<ResponseDto<AuthorResponseDto>> updateAuthor(
            @PathVariable Long authorId,
            @Valid @RequestBody AuthorRequestDto dto
    ) {
        ResponseDto<AuthorResponseDto> response = authorService.updateAuthor(authorId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 작가 삭제
    @DeleteMapping("/{authorId}")
    public ResponseEntity<ResponseDto<Void>> deleteAuthor(
            @PathVariable Long authorId
    ) {
        ResponseDto<Void> response = authorService.deleteAuthor(authorId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}
