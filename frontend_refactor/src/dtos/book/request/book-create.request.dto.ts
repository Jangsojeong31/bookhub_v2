export interface BookCreateRequestDto {
  isbn: string;
  categoryId: number;
  authorId: number;
  publisherId: number;
  bookTitle: string;
  bookPrice: number;
  publishedDate: string;
  coverUrl?: string;
  pageCount: string;
  language: string;
  description?: string;
  policyId?: number;
}