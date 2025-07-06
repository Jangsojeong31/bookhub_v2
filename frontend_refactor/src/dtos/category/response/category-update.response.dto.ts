export interface CategoryUpdateResponseDto {
  categoryId: number;
  categoryName: string;
  categoryLevel: number;
  categoryType: 'DOMESTIC' | 'FOREIGN';
  parentCategory?: number;
  categoryOrder: number;
  isActive: boolean;
  discountPolicy?: number;
}