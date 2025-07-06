export interface EmployeeDetailResponseDto {
  employeeId: number;
  employeeNumber: number;
  employeeName: string;
  branchId: number;
  branchName: string;
  positionId: number;
  authorityId: number;
  positionName: string;
  authorityName: string;
  email: string;
  phoneNumber: string;
  birthDate: Date;
  status: "EMPLOYED" | "EXITED";
  createdAt: Date;
}