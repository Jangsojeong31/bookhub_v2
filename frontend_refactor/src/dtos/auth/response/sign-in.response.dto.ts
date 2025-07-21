import { Employee } from "@/stores/useEmployeeStore";

export interface SignInResponseDto {
  token: string;
  exprTime: number;
  employee: Employee;
}