export interface PageResponseDto<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
}

