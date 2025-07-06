export interface ReceptionListResponseDto {
  bookReceptionApprovalId: number;
  bookIsbn: string;
  bookTitle: string;
  branchName: string;
  purchaseOrderAmount: number;
  isReceptionApproved: boolean;
  receptionDateAt: Date;
  receptionEmployeeName: string;
}