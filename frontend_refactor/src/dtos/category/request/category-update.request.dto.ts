export interface CategoryUpdateRequestDto {
  categoryName?: string;
  categoryType?: 'DOMESTIC' | 'FOREIGN';
  parentCategoryId?: number; // 소분류일때만 필요
  discountPolicyId?: number;
  isActive?: boolean;
}