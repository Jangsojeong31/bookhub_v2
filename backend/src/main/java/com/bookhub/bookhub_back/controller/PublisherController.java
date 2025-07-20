package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.branch.response.BranchResponseDto;
import com.bookhub.bookhub_back.dto.publisher.request.PublisherRequestDto;
import com.bookhub.bookhub_back.dto.publisher.response.PublisherResponseDto;
import com.bookhub.bookhub_back.service.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "출판사 생성")
    public ResponseEntity<ResponseDto<PublisherResponseDto>> createPublisher(
            @Valid @RequestBody PublisherRequestDto dto
    ){
        ResponseDto<PublisherResponseDto> response = publisherService.createPublisher(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    // 출판사 조회
    @GetMapping
    @Operation(summary = "출판사 조회")
    public ResponseEntity<ResponseDto<List<PublisherResponseDto>>> getPublishers(
            @RequestParam(required = false) String publisherName
    ) {
        ResponseDto<List<PublisherResponseDto>> response = publisherService.getPublishers(publisherName);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 출판사 수정
    @PutMapping("/{publisherId}")
    @Operation(summary = "출판사 수정")
    public ResponseEntity<ResponseDto<PublisherResponseDto>> updatePublisher(
            @PathVariable Long publisherId,
            @Valid @RequestBody PublisherRequestDto dto
    ){
        ResponseDto<PublisherResponseDto> response = publisherService.updatePublisher(publisherId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 출판사 삭제
    @DeleteMapping("/{publisherId}")
    @Operation(summary = "출판사 삭제")
    public ResponseEntity<ResponseDto<Void>> deletePublisher(
            @PathVariable Long publisherId
    ){
        ResponseDto<Void> response = publisherService.deletePublisher(publisherId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}
