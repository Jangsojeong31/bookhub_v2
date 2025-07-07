package com.bookhub.bookhub_back.dto.category.response;

import com.bookhub.bookhub_back.common.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryCreateResponseDto {
    private Long categoryId;
    private String categoryName;
    private int categoryLevel;
    private CategoryType categoryType;
    private int categoryOrder;
}
