package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.common.enums.CategoryType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.category.request.CategoryRequestDto;
import com.bookhub.bookhub_back.dto.category.response.CategoryResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface BookCategoryService {

    ResponseDto<CategoryResponseDto> createCategory(@Valid CategoryRequestDto dto);

    ResponseDto<CategoryResponseDto> updateCategory(Long categoryId, @Valid CategoryRequestDto dto);

    ResponseDto<Void> deleteCategory(Long categoryId);

    ResponseDto<List<CategoryResponseDto>> getCategoryTree(CategoryType type);

    ResponseDto<List<CategoryResponseDto>> getParentCategories();
}
