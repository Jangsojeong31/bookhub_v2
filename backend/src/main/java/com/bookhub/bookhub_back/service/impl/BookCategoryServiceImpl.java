package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.common.enums.CategoryType;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.category.request.CategoryCreateRequestDto;
import com.bookhub.bookhub_back.dto.category.request.CategoryUpdateRequestDto;
import com.bookhub.bookhub_back.dto.category.response.CategoryCreateResponseDto;
import com.bookhub.bookhub_back.dto.category.response.CategoryTreeResponseDto;
import com.bookhub.bookhub_back.dto.category.response.CategoryUpdateResponseDto;
import com.bookhub.bookhub_back.dto.policy.response.DiscountPolicyDetailResponseDto;
import com.bookhub.bookhub_back.entity.BookCategory;
import com.bookhub.bookhub_back.entity.DiscountPolicy;
import com.bookhub.bookhub_back.exception.BusinessException;
import com.bookhub.bookhub_back.exception.DuplicateEntityException;
import com.bookhub.bookhub_back.exception.ReferencedEntityException;
import com.bookhub.bookhub_back.repository.BookCategoryRepository;
import com.bookhub.bookhub_back.repository.BookRepository;
import com.bookhub.bookhub_back.repository.DiscountPolicyRepository;
import com.bookhub.bookhub_back.service.BookCategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookCategoryServiceImpl implements BookCategoryService {
    private final BookCategoryRepository bookCategoryRepository;
    private final DiscountPolicyRepository discountPolicyRepository;
    private final BookRepository bookRepository;

    // 카테고리 등록
    @Override
    public ResponseDto<CategoryCreateResponseDto> createCategory(CategoryCreateRequestDto dto) {

        boolean isCategoryExists;

        if (dto.getParentCategoryId() == null) {
            isCategoryExists = bookCategoryRepository.existsByCategoryNameAndCategoryTypeAndParentCategoryIdIsNull(
                    dto.getCategoryName(), dto.getCategoryType());
        } else {
            isCategoryExists = bookCategoryRepository.existsByCategoryNameAndCategoryTypeAndParentCategoryId_CategoryId(
                    dto.getCategoryName(), dto.getCategoryType(), dto.getParentCategoryId());
        }

        if (isCategoryExists) {
            throw new DuplicateEntityException("이미 존재하는 카테고리입니다.");
        }

        BookCategory parent = dto.getParentCategoryId() != null
                ? bookCategoryRepository.findById(dto.getParentCategoryId())
                .orElseThrow(EntityNotFoundException::new)
                : null;

        BookCategory newCategory = BookCategory.builder()
                .categoryName(dto.getCategoryName())
                .categoryLevel(dto.getCategoryLevel())
                .categoryType(dto.getCategoryType())
                .isActive(true) // 기본값으로 활성 상태로 생성
                .parentCategoryId(parent)
                .build();

        BookCategory savedCategory = bookCategoryRepository.save(newCategory);

        CategoryCreateResponseDto responseDto = CategoryCreateResponseDto.builder()
                .categoryId(savedCategory.getCategoryId())
                .categoryName(savedCategory.getCategoryName())
                .categoryLevel(savedCategory.getCategoryLevel())
                .categoryType(savedCategory.getCategoryType())
                .categoryOrder(savedCategory.getCategoryOrder())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // 트리 구조 조회
    @Override
    public ResponseDto<List<CategoryTreeResponseDto>> getCategoryTree(CategoryType type) {
        List<BookCategory> rootCategories = bookCategoryRepository.findByTypeAndLevel(type, 1);

        List<CategoryTreeResponseDto> result = rootCategories.stream()
                .map(this::buildTree)
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, result);
    }

    // 대분류 카테고리 조회
    @Override
    public ResponseDto<List<CategoryTreeResponseDto>> getRootCategories() {
        List<CategoryTreeResponseDto> result = bookCategoryRepository.findRootCategories().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, result);
    }

    // 카테고리 수정
    @Override
    public ResponseDto<CategoryUpdateResponseDto> updateCategory(Long categoryId, CategoryUpdateRequestDto dto) {
        BookCategory category = bookCategoryRepository.findById(categoryId)
                .orElseThrow(EntityNotFoundException::new);

        boolean isCategoryExists;

        if (category.getParentCategoryId() == null) {
            isCategoryExists = bookCategoryRepository.existsByCategoryNameAndCategoryTypeAndParentCategoryIdIsNullAndCategoryIdNot(
                    dto.getCategoryName(), dto.getCategoryType(), categoryId);
        } else {
            isCategoryExists = bookCategoryRepository.existsByCategoryNameAndCategoryTypeAndParentCategoryId_CategoryIdAndCategoryIdNot(
                dto.getCategoryName(), dto.getCategoryType(), category.getParentCategoryId().getCategoryId(), categoryId);
        }

        if (isCategoryExists) {
            throw new DuplicateEntityException("이미 존재하는 카테고리입니다.");
        }

        DiscountPolicy policy = dto.getDiscountPolicyId() != null
                ? discountPolicyRepository.findById(dto.getDiscountPolicyId())
                .orElseThrow(EntityNotFoundException::new)
                : null;

        category.setCategoryName(dto.getCategoryName());
        category.setCategoryType(dto.getCategoryType());
        category.setDiscountPolicyId(policy);

        BookCategory updatedCategory = bookCategoryRepository.save(category);

        CategoryUpdateResponseDto responseDto = CategoryUpdateResponseDto.builder()
                .parentCategoryId(updatedCategory.getParentCategoryId() != null ? category.getParentCategoryId().getCategoryId() : null)
                .categoryName(updatedCategory.getCategoryName())
                .categoryLevel(updatedCategory.getCategoryLevel())
                .categoryType(updatedCategory.getCategoryType().toString())
                .categoryOrder(updatedCategory.getCategoryOrder())
                .isActive(updatedCategory.getIsActive())
                .discountPolicyId(updatedCategory.getDiscountPolicyId() != null ? category.getDiscountPolicyId().getPolicyId() : null)
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // 카테고리 비활성화 / 활성화
    @Override
    public ResponseDto<Void> updateCategoryStatus(Long categoryId) {
        BookCategory category = bookCategoryRepository.findById(categoryId)
                .orElseThrow(EntityNotFoundException::new);

        if (bookRepository.existsByCategoryId_CategoryId(categoryId)) {
            throw new ReferencedEntityException("참조 중인 카테고리는 비활성화할 수 없습니다.");
        }

        category.setIsActive(!category.getIsActive());

        bookCategoryRepository.save(category);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    @Override
    public ResponseDto<DiscountPolicyDetailResponseDto> getPolicyByCategoryId(Long categoryId) {
        BookCategory category = bookCategoryRepository.findById(categoryId)
                .orElseThrow(EntityNotFoundException::new);

        DiscountPolicy policy = category.getDiscountPolicyId();

        // 정책이 없는 경우 null 응답
        if (policy == null) {
            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, null);
        }

        DiscountPolicyDetailResponseDto responseDto = DiscountPolicyDetailResponseDto.builder()
                .policyTitle(policy.getPolicyTitle())
                .policyDescription(policy.getPolicyDescription())
                .policyType(policy.getPolicyType())
                .totalPriceAchieve(policy.getTotalPriceAchieve())
                .discountPercent(policy.getDiscountPercent())
                .startDate(policy.getStartDate())
                .endDate(policy.getEndDate())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // CategoryTreeResponseDto 변환 메서드
    private CategoryTreeResponseDto toResponseDto(BookCategory category) {
        return CategoryTreeResponseDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .categoryLevel(category.getCategoryLevel())
                .categoryType(category.getCategoryType())
                .categoryOrder(category.getCategoryOrder())
                .isActive(category.getIsActive())
                .parentCategoryId(
                        category.getParentCategoryId() != null
                                ? category.getParentCategoryId().getCategoryId()
                                : null
                )
                .discountPolicyId(
                        category.getDiscountPolicyId() != null
                                ? category.getDiscountPolicyId().getPolicyId()
                                : null
                )
                .subCategories(new ArrayList<>())
                .build();
    }

    // 하위 카테고리를 포함한 트리 구조로 변환 메서드
    private CategoryTreeResponseDto buildTree(BookCategory parent) {
        CategoryTreeResponseDto dto = toResponseDto(parent);
        List<BookCategory> children = bookCategoryRepository.findByParentId(parent.getCategoryId());
        if (!children.isEmpty()) {
            List<CategoryTreeResponseDto> childDtos = children.stream()
                    .map(this::buildTree)
                    .collect(Collectors.toList());
            dto.setSubCategories(childDtos);
        }
        return dto;
    }
}