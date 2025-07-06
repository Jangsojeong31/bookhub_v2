//src/dtos/stock/StockLog.response.dto.ts


import { StockActionType } from "@/apis/enums/StockActionType";

export interface StockLogResponseDto{
  stockLogId : number;
  type : StockActionType;
  employeName : string;
  bookTitle : string;
  branchName : string;
  amount : number;
  bookAmount : number;
  actionDate: string;
  description : string;
}

export interface StockLogDetailResponseDto{
  stockLogId : number;
  type : StockActionType;
  employeName : string;
  bookTitle : string;
  branchName : string;
  amount : number;
  bookAmount : number;
  actionDate: string;
  description : string;
}