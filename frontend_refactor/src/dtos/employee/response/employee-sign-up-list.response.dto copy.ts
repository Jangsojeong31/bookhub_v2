export interface EmployeeSignUpListResponseDto {
  approvalId: number;
  employeeId: number;
  employeeNumber: string;
  employeeName: string;
  branchName: string;
  email: string;
  phoneNumber: string;
  appliedAt: Date;
  isApproved: string;
}
