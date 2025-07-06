import { Employee } from "@/stores/employee.store";

export interface SignInResponseDto {
  token: string;
  exprTime: number;
  employee: Employee;
}