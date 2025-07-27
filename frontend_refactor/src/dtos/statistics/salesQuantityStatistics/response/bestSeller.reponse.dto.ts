export interface BestSellerResponseDto {
  bookIsbn: string;
  bookTitle: string;
  authorName: string;
  categoryName: string;
  publisherName: string;
  coverUrl?: string;
  totalSales: number;
}