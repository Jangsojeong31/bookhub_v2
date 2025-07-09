// src/apis/stockLog.ts
import { axiosInstance, bearerAuthorization } from '@/apis/axiosConfig';
import * as urls from '@/apis/constants/csy.constants';
import {
  StockLogResponseDto,
  StockLogDetailResponseDto,
} from '@/dtos/stock/StockLog.response.dto';



// branch 전체
export const getStockLogsByBranch = async (
  branchId: number, 
  type: string,
  bookIsbn: string,
  start: string,
  end: string,
  accessToken: string) => {
  const res = await axiosInstance.get(urls.STOCK_LOGS_BY_BRANCH(branchId, type, bookIsbn, start, end), bearerAuthorization(accessToken));
  return res.data;
};

// employee
export const getStockLogsByEmployee = async (employeeId: number, accessToken: string) => {
  const res = await axiosInstance.get(urls.STOCK_LOGS_BY_EMPLOYEE(employeeId), bearerAuthorization(accessToken));
  return res.data;
};


