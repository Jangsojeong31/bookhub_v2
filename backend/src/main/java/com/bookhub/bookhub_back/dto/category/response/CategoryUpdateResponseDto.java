package com.bookhub.bookhub_back.dto.category.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUpdateResponseDto {
    private String categoryName;
    private int categoryLevel;
    private String categoryType;
    private int categoryOrder;
    private boolean isActive;
    private Long parentCategoryId;
    private Long discountPolicyId;
}
