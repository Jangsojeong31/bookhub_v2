// src/apis/stockLog.ts
import { axiosInstance, bearerAuthorization } from '@/apis/axiosConfig';
import * as urls from '@/apis/constants/csy.constants';
import {
  StockLogResponseDto,
  StockLogDetailResponseDto,
} from '@/dtos/stock/StockLog.response.dto';

// 조건별 조회
export const getStockLogs = async (
  branchName: string, 
  type: string,
  bookIsbn: string,
  start: string,
  end: string,
  accessToken: string) => {
  const res = await axiosInstance.get(urls.SEARCH_STOCK_LOGS_URL(branchName, type, bookIsbn, start, end), bearerAuthorization(accessToken));
  return res.data;
};


// branch 전체
export const getStockLogsByBranch = async (
  type: string,
  bookIsbn: string,
  start: string,
  end: string,
  accessToken: string) => {
  const res = await axiosInstance.get(urls.STOCK_LOGS_BY_BRANCH(type, bookIsbn, start, end), bearerAuthorization(accessToken));
  return res.data;
};

