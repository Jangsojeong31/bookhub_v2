import { ResponseDto } from "@/dtos";
import { StockUpdateRequestDto } from "@/dtos/stock/Stock.request.dto";
import { StockCreateResponseDto, StockUpdateResponseDto, StockListResponseDto } from "@/dtos/stock/Stock.response.dto";
import axios from "axios";
import { UPDATE_STOCK_URL, STOCK_SEARCH_BOOK_URL, STOCK_SEARCH_TITLE_URL, STOCK_SEARCH_BRANCH_URL } from "../constants/csy.constants";



export const updateStock = (
 stockId: number, dto: StockUpdateRequestDto, accessToken: string): Promise<ResponseDto<StockUpdateResponseDto>> =>
  axios
    .put(UPDATE_STOCK_URL(stockId), dto, {
      headers: { Authorization: `Bearer ${accessToken}` },
    })
    .then((res) => res.data);

export const getStockByIsbn = (
  isbn: string,
  accessToken: string
): Promise<ResponseDto<StockListResponseDto[]>> =>
  axios
    .get(STOCK_SEARCH_BOOK_URL(isbn), { headers: { Authorization: `Bearer ${accessToken}` } })
    .then((res) => res.data);

export const getStockByTitle = (
  title: string,
  accessToken: string
): Promise<ResponseDto<StockListResponseDto[]>> =>
  axios
    .get(STOCK_SEARCH_TITLE_URL, {
      headers: { Authorization: `Bearer ${accessToken}` },
      params: { bookTitle: title },
    })
    .then((res) => res.data);

export const getStockByBranch = (
  branchId: number,
  accessToken: string
): Promise<ResponseDto<StockListResponseDto[]>> =>
  axios
    .get(STOCK_SEARCH_BRANCH_URL(branchId), { headers: { Authorization: `Bearer ${accessToken}` } })
    .then((res) => res.data);