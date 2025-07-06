package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.common.enums.CategoryType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.category.request.CategoryRequestDto;
import com.bookhub.bookhub_back.dto.category.response.CategoryResponseDto;
import com.bookhub.bookhub_back.service.BookCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.AUTH + "/categories")
@RequiredArgsConstructor
public class BookCategoryController {
    private final BookCategoryService bookCategoryService;

    // 카테고리 등록
    @PostMapping
    public ResponseEntity<ResponseDto<CategoryResponseDto>> createCategory(
            @Valid @RequestBody CategoryRequestDto dto
    ) {
        ResponseDto<CategoryResponseDto> response = bookCategoryService.createCategory(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    // 카테고리 수정
    @PutMapping("/{categoryId}")
    public ResponseEntity<ResponseDto<CategoryResponseDto>> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequestDto dto
    ) {
        ResponseDto<CategoryResponseDto> response = bookCategoryService.updateCategory(categoryId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 카테고리 삭제(비활성화)
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ResponseDto<Void>> deleteCategory(
            @PathVariable Long categoryId
    ) {
        ResponseDto<Void> response = bookCategoryService.deleteCategory(categoryId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 트리형 카테고리 조회
    @GetMapping("/tree")
    public ResponseEntity<ResponseDto<List<CategoryResponseDto>>> getCategoryTree(
            @RequestParam CategoryType type
    ) {
        ResponseDto<List<CategoryResponseDto>> response = bookCategoryService.getCategoryTree(type);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 대분류 카테고리 목록 조회
    @GetMapping("/parentCategories")
    public ResponseEntity<ResponseDto<List<CategoryResponseDto>>> getParentCategories(
    ) {
        ResponseDto<List<CategoryResponseDto>> response = bookCategoryService.getParentCategories();
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }
}
