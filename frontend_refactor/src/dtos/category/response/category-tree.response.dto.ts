export interface CategoryTreeResponseDto {
  categoryId: number;
  categoryName: string;
  categoryLevel: number;
  categoryType: 'DOMESTIC' | 'FOREIGN';
  parentCategoryId?: number;
  categoryOrder?: number;
  isActive: boolean;
  discountPolicyId?: number;
  subCategories?: CategoryTreeResponseDto[];
}