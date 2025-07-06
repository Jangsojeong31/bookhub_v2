export interface CategoryCreateRequestDto {
  categoryName: string;
  categoryType: 'DOMESTIC' | 'FOREIGN';
  categoryLevel: 1 | 2;
  categoryOrder?: number;
  parentCategoryId?: number; // 소분류일때만 필요
}