package com.bookhub.bookhub_back.dto.category.response;

import com.bookhub.bookhub_back.common.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CategoryResponseDto {
    private Long categoryId;
    private String categoryName;
    private int categoryLevel;
    private CategoryType categoryType;
    private int categoryOrder;
    private boolean isActive;
    private Long discountPolicyId;
    private Long parentCategoryId;
    private List<CategoryResponseDto> subCategories;
}
