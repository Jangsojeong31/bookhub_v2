export interface EmployeeListResponseDto {
  employeeId: number;
  employeeNumber: number;
  employeeName: string;
  branchName: string;
  positionName: string;
  authorityName: string;
  status: "EMPLOYED" | "EXITED";
}
