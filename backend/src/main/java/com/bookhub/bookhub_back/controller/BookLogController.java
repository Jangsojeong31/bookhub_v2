package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.book.response.BookLogResponseDto;
import com.bookhub.bookhub_back.service.BookLogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.ADMIN + "/book-logs")
@RequiredArgsConstructor
public class BookLogController {
    private final BookLogService bookLogService;

    // 로그 목록 조회 (isbn으로)
    @GetMapping
    @Operation(summary = "책 수정 기록 조회 (isbn으로)")
    public ResponseEntity<ResponseDto<List<BookLogResponseDto>>> getLogsByBook(
            @RequestParam String isbn
    ) {
        ResponseDto<List<BookLogResponseDto>> bookLog = bookLogService.getLogsByBook(isbn);
        return ResponseEntity.status(HttpStatus.OK).body(bookLog);
    }
}
