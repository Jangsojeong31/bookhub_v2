//src/dtos/stock/Stock.request.dto.ts
import { StockActionType } from "@/apis/enums/StockActionType";

export interface StockUpdateRequestDto {
  type: StockActionType;
  employeeId: number;
  bookIsbn: string;
  branchId: number;
  amount: number;
  description: string;
}