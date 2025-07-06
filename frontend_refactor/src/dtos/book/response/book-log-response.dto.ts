export interface BookLogResponseDto {
  bookLogId: number;
  bookIsbn: string;
  bookTitle: string;
  bookLogType: "CREATE" | "PRICE_CHANGE" | "DISCOUNT_RATE" | "STATUS_CHANGE" | "HIDDEN";
  previousPrice?: number;
  previousDiscountRate?: number;
  employeeName: string;
  policyId?: number;
  changedAt: Date;
}