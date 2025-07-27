import { ResponseDto } from "@/dtos";
import { StockUpdateRequestDto } from "@/dtos/stock/Stock.request.dto";
import {
  StockUpdateResponseDto,
  StockListResponseDto,
} from "@/dtos/stock/Stock.response.dto";
import { AxiosError } from "axios";
import {
  UPDATE_STOCK_URL,
  SEARCH_STOCKS_BY_BRANCH_URL,
  SEARCH_STOCKS_URL,
} from "../../constants/api/csy.constants";
import {
  axiosInstance,
  bearerAuthorization,
  responseErrorHandler,
  responseSuccessHandler,
} from "../axiosConfig";

export const updateStock = async (
  stockId: number,
  dto: StockUpdateRequestDto,
  accessToken: string
): Promise<ResponseDto<StockUpdateResponseDto>> => {
  try {
    const response = await axiosInstance.put(
      UPDATE_STOCK_URL(stockId),
      dto,
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const searchStocksByBranch = async (
  bookTitle: string,
  isbn: string,
  accessToken: string
): Promise<ResponseDto<StockListResponseDto[]>> => {
  try {
    const response = await axiosInstance.get(
      SEARCH_STOCKS_BY_BRANCH_URL(bookTitle, isbn),
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const searchStocks = async (
  bookTitle: string,
  isbn: string,
  branchName: string,
  accessToken: string
): Promise<ResponseDto<StockListResponseDto[]>> => {
  try {
    const response = await axiosInstance.get(
      SEARCH_STOCKS_URL(bookTitle, isbn, branchName),
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};
