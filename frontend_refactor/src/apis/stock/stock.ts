import { ResponseDto } from "@/dtos";
import { StockUpdateRequestDto } from "@/dtos/stock/Stock.request.dto";
import { StockCreateResponseDto, StockUpdateResponseDto, StockListResponseDto } from "@/dtos/stock/Stock.response.dto";
import axios, { Axios } from "axios";
import { UPDATE_STOCK_URL, SEARCH_STOCKS_BY_BRANCH_URL, SEARCH_STOCKS_URL } from "../constants/csy.constants";
import { bearerAuthorization } from "../axiosConfig";
import { access } from "fs";

export const updateStock = (
  stockId: number, dto: StockUpdateRequestDto, accessToken: string): Promise<ResponseDto<StockUpdateResponseDto>> =>
  axios
    .put(UPDATE_STOCK_URL(stockId), dto, bearerAuthorization(accessToken))
    .then((res) => res.data);


export const searchStocksByBranch = (
  bookTitle: string,
  isbn: string,
  accessToken: string
): Promise<ResponseDto<StockListResponseDto[]>> =>
  axios
    .get(SEARCH_STOCKS_BY_BRANCH_URL(bookTitle, isbn), bearerAuthorization(accessToken))
    .then((res) => res.data);

export const searchStocks = (
  bookTitle: string,
  isbn: string,
  branchName: string,
  accessToken: string
): Promise<ResponseDto<StockListResponseDto[]>> =>
  axios
    .get(SEARCH_STOCKS_URL(bookTitle, isbn, branchName), bearerAuthorization(accessToken))
    .then((res) => res.data);
