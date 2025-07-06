type ChangeType = "POSITION_CHANGE" | "AUTHORITY_CHANGE" | "BRANCH_CHANGE"

export interface EmployeeChangeLogsResponseDto {
  logId: number;
  employeeNumber: number;
  employeeName: string;
  changeType: ChangeType;
  prePositionName: string | null;
  preAuthorityName: string | null;
  preBranchName: string | null;
  authorizerNumber: number;
  authorizerName: string;
  updatedAt: Date
}