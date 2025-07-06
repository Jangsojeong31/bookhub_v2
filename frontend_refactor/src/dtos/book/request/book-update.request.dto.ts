export interface BookUpdateRequestDto {
  isbn: string;
  bookPrice?: number;
  description?: string;
  bookStatus?: 'ACTIVE' | 'INACTIVE' | 'HIDDEN';
  policyId?: number | null;
  categoryId?: number | null;
}
