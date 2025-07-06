type status = "EMPLOYED" | "EXITED";
type exitReason = "VOLUNTEER" | "FORCED" | "TERMINATED" | "RETIREMENT";

export interface EmployeeExitLogsResponseDto {
  exitId: number;
  employeeNumber: number;
  employeeName: string;
  branchName: string;
  positionName: string;
  status: status;
  exitReason: exitReason;
  authorizerNumber: number;
  authorizerName: string;
  updatedAt: Date;
}
