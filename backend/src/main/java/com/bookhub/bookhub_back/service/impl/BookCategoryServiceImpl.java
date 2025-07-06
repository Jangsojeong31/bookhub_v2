package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.common.enums.CategoryType;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.category.request.CategoryRequestDto;
import com.bookhub.bookhub_back.dto.category.response.CategoryResponseDto;
import com.bookhub.bookhub_back.entity.BookCategory;
import com.bookhub.bookhub_back.entity.DiscountPolicy;
import com.bookhub.bookhub_back.repository.BookCategoryRepository;
import com.bookhub.bookhub_back.repository.DiscountPolicyRepository;
import com.bookhub.bookhub_back.service.BookCategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookCategoryServiceImpl implements BookCategoryService {
    private final BookCategoryRepository bookCategoryRepository;
    private final DiscountPolicyRepository discountPolicyRepository;

    // 카테고리 등록
    @Override
    public ResponseDto<CategoryResponseDto> createCategory(CategoryRequestDto dto) {
        BookCategory parentCategory = null;
        DiscountPolicy discountPolicy = null;

        // 부모 카테고리가 있는 경우 조회
        if (dto.getParentCategoryId() != null) {
            parentCategory = bookCategoryRepository.findById(dto.getParentCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException());
        }

        // 적용된 할인 정책이 있는 경우 조회
        if (dto.getDiscountPolicyId() != null) {
            discountPolicy = discountPolicyRepository.findById(dto.getDiscountPolicyId())
                    .orElseThrow(() -> new EntityNotFoundException());
        }

        BookCategory newCategory = BookCategory.builder()
                .categoryName(dto.getCategoryName())
                .categoryLevel(dto.getCategoryLevel())
                .categoryType(dto.getCategoryType())
                .parentCategoryId(parentCategory) // 부모 카테고리가 없으면 null
                .discountPolicyId(discountPolicy) // 적용된 할인 정책이 없으면 null
                .build();

        BookCategory savedCategory = bookCategoryRepository.save(newCategory);

        CategoryResponseDto responseDto = toResponseDto(savedCategory);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // 카테고리 수정
    @Override
    public ResponseDto<CategoryResponseDto> updateCategory(Long categoryId, CategoryRequestDto dto) {
        BookCategory parentCategory = null;
        DiscountPolicy discountPolicy = null;

        BookCategory category = bookCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리가 존재하지 않습니다."));

        // 부모 카테고리가 있는 경우 조회
        if (dto.getParentCategoryId() != null) {
            parentCategory = bookCategoryRepository.findById(dto.getParentCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException());
        }

        // 적용된 할인 정책이 있는 경우 조회
        if (dto.getDiscountPolicyId() != null) {
            discountPolicy = discountPolicyRepository.findById(dto.getDiscountPolicyId())
                    .orElseThrow(() -> new EntityNotFoundException());
        }

        category.setCategoryName(dto.getCategoryName());
        category.setCategoryLevel(dto.getCategoryLevel());
        category.setCategoryType(dto.getCategoryType());
        category.setCategoryOrder(dto.getCategoryOrder());
        category.setDiscountPolicyId(discountPolicy);
        category.setParentCategoryId(parentCategory);

        BookCategory updatedCategory = bookCategoryRepository.save(category);

        CategoryResponseDto responseDto = toResponseDto(updatedCategory);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // 카테고리 삭제(비활성화)
    @Override
    public ResponseDto<Void> deleteCategory(Long categoryId) {
        BookCategory category = bookCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리가 존재하지 않습니다."));

        category.setIsActive(false);
        bookCategoryRepository.save(category);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    // 트리 구조 조회
    @Override
    public ResponseDto<List<CategoryResponseDto>> getCategoryTree(CategoryType type) {
        List<BookCategory> rootCategories = bookCategoryRepository.findByCategoryTypeAndCategoryLevel(type, 1);
        List<CategoryResponseDto> result = rootCategories.stream()
                .map(this::buildTree)
                .collect(Collectors.toList());
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, result);
    }

    // 대분류 카테고리 목록 조회
    @Override
    public ResponseDto<List<CategoryResponseDto>> getParentCategories() {
        List<BookCategory> categories = bookCategoryRepository.findAllByParentCategoryIdIsNull();
        List<CategoryResponseDto> responseDtos = categories.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    // responseDto 변환 메서드
    private CategoryResponseDto toResponseDto(BookCategory category) {
        CategoryResponseDto responseDto = CategoryResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .categoryLevel(category.getCategoryLevel())
                .categoryType(category.getCategoryType())
                .categoryOrder(category.getCategoryOrder())
                .parentCategoryId(category.getParentCategoryId().getCategoryId())
                .discountPolicyId(category.getDiscountPolicyId().getPolicyId())
                .isActive(category.getIsActive())
                .build();

        return responseDto;
    }

    // 재귀적으로 하위 카테고리를 포함한 트리 구조로 변환
    private CategoryResponseDto buildTree(BookCategory parent) {
        CategoryResponseDto dto = toResponseDto(parent);
        List<BookCategory> childrenCategories = bookCategoryRepository.findAllByParentCategoryId(parent);
        if (!childrenCategories.isEmpty()) {
            List<CategoryResponseDto> childDtos = childrenCategories.stream()
                    .map(this::buildTree)
                    .collect(Collectors.toList());
            dto.setSubCategories(childDtos);
        }
        return dto;
    }
}