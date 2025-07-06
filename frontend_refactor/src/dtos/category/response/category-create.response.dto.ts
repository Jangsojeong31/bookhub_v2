export interface CategoryCreateResponseDto {
  categoryId: number;
  categoryName: string;
  categoryLevel: number;
  categoryType: 'DOMESTIC' | 'FOREIGN';
  categoryOrder: number;
}