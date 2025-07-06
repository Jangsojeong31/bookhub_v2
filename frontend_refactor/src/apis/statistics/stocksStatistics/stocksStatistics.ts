import {
  axiosInstance,
  bearerAuthorization,
  responseErrorHandler,
  responseSuccessHandler,
} from "@/apis/axiosConfig";
import {
  STOCK_STATISTICS_BRANCH_URL,
  STOCK_STATISTICS_CATEGORY_URL,
  STOCK_STATISTICS_TIME_URL,
  STOCK_STATISTICS_ZERO_URL,
} from "@/apis/constants/csy.constants";
import { ResponseDto } from "@/dtos";
import { BranchStockBarChartResponseDto } from "@/dtos/statistics/StocksStatistics/response/branchStockBarChart.response.dto";
import { CategoryStockResponseDto } from "@/dtos/statistics/StocksStatistics/response/categoryStock.response.dto";
import { TimeStockChartResponseDto } from "@/dtos/statistics/StocksStatistics/response/timestockchart.response.dto";
import { ZeroStockResponseDto } from "@/dtos/statistics/StocksStatistics/response/zeroStock.response.dto";
import { AxiosError } from "axios";
import { access } from "fs";

interface searchDateParams {
  year: number;
  month: number;
}

interface searchBranchParams {
  branchName: string;
}

interface searcYearParams {
  year: number;
}

export const branchStockBarChartRequest = async (
  params: searchDateParams,
  accessToken: string
): Promise<ResponseDto<BranchStockBarChartResponseDto[]>> => {
  try {
    const response = await axiosInstance.get(STOCK_STATISTICS_BRANCH_URL, {
      params,
      ...bearerAuthorization(accessToken),
    });
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const categoryStockRequest = async (
  params: searchBranchParams,
  accessToken: string
): Promise<ResponseDto<CategoryStockResponseDto[]>> => {
  try {
    const response = await axiosInstance.get(STOCK_STATISTICS_CATEGORY_URL, {
      params,
      ...bearerAuthorization(accessToken),
    });
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const zeroStockRequest = async (
  accessToken: string
): Promise<ResponseDto<ZeroStockResponseDto[]>> => {
  try {
    const response = await axiosInstance.get(
      STOCK_STATISTICS_ZERO_URL,
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const timeStockChartrequest = async (
  params: searcYearParams,
  accessToken: string
): Promise<ResponseDto<TimeStockChartResponseDto[]>> => {
  try {
    const response = await axiosInstance.get(STOCK_STATISTICS_TIME_URL, {
      params,
      ...bearerAuthorization(accessToken),
    });
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};
