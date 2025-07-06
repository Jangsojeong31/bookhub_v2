export interface EmployeeExitUpdateRequestDto {
  status: "EMPLOYED" | "EXITED";
  exitReason: string;
}