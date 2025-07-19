package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.common.enums.CategoryType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.category.request.CategoryCreateRequestDto;
import com.bookhub.bookhub_back.dto.category.request.CategoryUpdateRequestDto;
import com.bookhub.bookhub_back.dto.category.response.CategoryCreateResponseDto;
import com.bookhub.bookhub_back.dto.category.response.CategoryTreeResponseDto;
import com.bookhub.bookhub_back.dto.category.response.CategoryUpdateResponseDto;
import com.bookhub.bookhub_back.dto.policy.response.DiscountPolicyDetailResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface BookCategoryService {

    ResponseDto<CategoryCreateResponseDto> createCategory(@Valid CategoryCreateRequestDto dto);

    ResponseDto<List<CategoryTreeResponseDto>> getCategoryTree(CategoryType type);

    ResponseDto<List<CategoryTreeResponseDto>> getRootCategories();

    ResponseDto<CategoryUpdateResponseDto> updateCategory(Long categoryId, CategoryUpdateRequestDto dto);

    ResponseDto<Void> updateCategoryStatus(Long categoryId);

    ResponseDto<DiscountPolicyDetailResponseDto> getPolicyByCategoryId(Long categoryId);
}
