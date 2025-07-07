package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.publisher.request.PublisherRequestDto;
import com.bookhub.bookhub_back.dto.publisher.response.PublisherResponseDto;
import com.bookhub.bookhub_back.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.ADMIN + "/publishers")
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherService publisherService;

    // 출판사 생성
    @PostMapping
    public ResponseEntity<ResponseDto<PublisherResponseDto>> createPublisher(
            @Valid @RequestBody PublisherRequestDto dto
    ){
        ResponseDto<PublisherResponseDto> response = publisherService.createPublisher(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 출판사 조회 (키워드 없을 경우 전체 조회)
    @GetMapping
    public ResponseEntity<ResponseDto<?>> getPublishers(
            @RequestParam(required = false) String keyword
    ) {
        if (keyword != null && !keyword.isBlank()) {
            ResponseDto<PublisherResponseDto> response = publisherService.getPublisherByName(keyword);
            return ResponseEntity.ok(response);
        }
        ResponseDto<List<PublisherResponseDto>> response = publisherService.getAllPublishers();
        return ResponseEntity.ok(response);
    }

    // 출판사 수정
    @PutMapping("/{publisherId}")
    public ResponseEntity<ResponseDto<PublisherResponseDto>> updatePublisher(
            @PathVariable Long publisherId,
            @Valid @RequestBody PublisherRequestDto dto
    ){
        ResponseDto<PublisherResponseDto> response = publisherService.updatePublisher(publisherId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 출판사 삭제하기
    @DeleteMapping("/{publisherId}")
    public ResponseEntity<ResponseDto<Void>> deletePublisher(
            @PathVariable Long publisherId
    ){
        ResponseDto<Void> response = publisherService.deletePublisher(publisherId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
