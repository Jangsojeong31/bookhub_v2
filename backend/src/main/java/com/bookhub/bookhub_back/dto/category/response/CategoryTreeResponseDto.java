package com.bookhub.bookhub_back.dto.category.response;

import com.bookhub.bookhub_back.common.enums.CategoryType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryTreeResponseDto {
    private Long categoryId;
    private String categoryName;
    private int categoryLevel;
    private CategoryType categoryType;
    private int categoryOrder;
    private Boolean isActive;
    private Long parentCategoryId;
    private Long discountPolicyId;
    private List<CategoryTreeResponseDto> subCategories;
}
