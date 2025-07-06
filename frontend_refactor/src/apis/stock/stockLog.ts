// src/apis/stockLog.ts
import { axiosInstance, bearerAuthorization } from '@/apis/axiosConfig';
import * as urls from '@/apis/constants/csy.constants';
import {
  StockLogResponseDto,
  StockLogDetailResponseDto,
} from '@/dtos/stock/StockLog.response.dto';



// branch 전체
export const getStockLogsByBranch = async (branchId: number, accessToken: string) => {
  const res = await axiosInstance.get(urls.STOCK_LOGS_BY_BRANCH(branchId), bearerAuthorization(accessToken));
  return res.data;
};

// branch + 유형
export const getStockLogsByType = async (branchId: number, type: string, accessToken: string) => {
  const res = await axiosInstance.get(urls.STOCK_LOGS_BY_TYPE(branchId, type), bearerAuthorization(accessToken));
  return res.data;
};

// branch + 날짜
export const getStockLogsByDate = async (
  branchId: number,
  start: string,
  end: string,
  accessToken: string
) => {
  const res = await axiosInstance.get(urls.STOCK_LOGS_BY_DATE(branchId, start, end), bearerAuthorization(accessToken));
  return res.data;
};

// employee
export const getStockLogsByEmployee = async (employeeId: number, accessToken: string) => {
  const res = await axiosInstance.get(urls.STOCK_LOGS_BY_EMPLOYEE(employeeId), bearerAuthorization(accessToken));
  return res.data;
};

// book
export const getStockLogsByBook = async (branchId: number, bookIsbn: string, accessToken: string) => {
  const res = await axiosInstance.get(urls.STOCK_LOGS_BY_BOOK(branchId, bookIsbn), bearerAuthorization(accessToken));
  return res.data;
};

// 단건 상세
export const getStockLogDetail = async (stockLogId: number, accessToken: string) => {
  const res = await axiosInstance.get(urls.STOCK_LOG_DETAIL(stockLogId), bearerAuthorization(accessToken));
  return res.data;
};
