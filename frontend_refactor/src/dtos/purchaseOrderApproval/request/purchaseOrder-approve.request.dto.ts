export enum PurchaseOrderStatus {
  REQUESTED = 'REQUESTED',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED'
}

export interface PurchaseOrderApproveRequestDto {
  status: PurchaseOrderStatus;
}