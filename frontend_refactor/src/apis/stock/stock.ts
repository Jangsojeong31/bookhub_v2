import { ResponseDto } from "@/dtos";
import { StockUpdateRequestDto } from "@/dtos/stock/Stock.request.dto";
import { StockCreateResponseDto, StockUpdateResponseDto, StockListResponseDto } from "@/dtos/stock/Stock.response.dto";
import axios from "axios";
import { UPDATE_STOCK_URL, STOCK_SEARCH_BOOK_URL } from "../constants/csy.constants";
import { bearerAuthorization } from "../axiosConfig";
import { access } from "fs";



export const updateStock = (
  stockId: number, dto: StockUpdateRequestDto, accessToken: string): Promise<ResponseDto<StockUpdateResponseDto>> =>
  axios
    .put(UPDATE_STOCK_URL(stockId), dto, bearerAuthorization(accessToken))
    .then((res) => res.data);

export const searchStocks = (
  bookTitle: string,
  isbn: string,
  branchName: string,
  accessToken: string
): Promise<ResponseDto<StockListResponseDto[]>> =>
  axios
    .get(STOCK_SEARCH_BOOK_URL(bookTitle, isbn, branchName), bearerAuthorization(accessToken))
    .then((res) => res.data);
