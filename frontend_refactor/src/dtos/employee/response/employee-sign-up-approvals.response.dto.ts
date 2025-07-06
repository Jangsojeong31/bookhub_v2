export interface EmployeeSignUpApprovalsResponseDto {
  approvalId: number;
  employeeNumber: number;
  employeeName: string;
  appliedAt: string;
  isApproved: "APPROVED" | "DENIED";
  deniedReason: string | undefined;
  authorizerNumber: number;
  authorizerName: string;
  updatedAt: string;
}
