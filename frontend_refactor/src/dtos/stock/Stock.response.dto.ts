//src/dtos/stock/Stock.response.dto.ts

import { StockActionType } from "@/apis/enums/StockActionType";

export interface StockListResponseDto {
  stockId: number;
  branchId: number;
  bookIsbn: string;
  branchName: string;
  bookTitle: string;
  amount: number;
}

export interface StockUpdateResponseDto {
  stockId: number;
  branchName: string;
  type: string;
  bookTitle: string;
  amount: number;
  bookAmount: number;
}



export interface StockCreateResponseDto {
  stockId: number;
  branchName: string;
  bookTitle: string;
  amount: number;
  bookAmount: number;
}