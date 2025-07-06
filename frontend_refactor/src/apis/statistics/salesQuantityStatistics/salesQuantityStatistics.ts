import { axiosInstance, bearerAuthorization, responseSuccessHandler, responseErrorHandler } from "@/apis/axiosConfig";
import { GET_BEST_SELLERS_BY_CATEGORY, GET_DAILY_SALES_QUANTITY, GET_MONTHLY_BEST_SELLERS, GET_MONTHLY_SALES_QUANTITY, GET_SALES_QUANTITY_BY_BRANCH, GET_SALES_QUANTITY_BY_CATEGORY, GET_SALES_QUANTITY_BY_DISCOUNT_POLICY, GET_TOP_100_BEST_SELLERS, GET_WEEKLY_BEST_SELLERS, GET_WEEKLY_SALES_QUANTITY, GET_YEARLY_BEST_SELLERS } from "@/apis/constants/jsj.constants";
import { ResponseDto } from "@/dtos";
import { BestSellerResponseDto } from "@/dtos/statistics/salesQuantityStatistics/response/bestSeller.reponse.dto";
import { CategorySalesQuantityResponseDto } from "@/dtos/statistics/salesQuantityStatistics/response/categorySalesQuantity.response.dto";
import { SalesQuantityStatisticsReponseDto } from "@/dtos/statistics/salesQuantityStatistics/response/salesQuantity.response.dto";
import { AxiosError } from "axios";

// 베스트셀러
// 총합 베스트셀러
export const getTop100BestSellers = async(accessToken: string): Promise<ResponseDto<BestSellerResponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_TOP_100_BEST_SELLERS, bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 주간 베스트셀러
export const getWeeklyBestSellers = async(accessToken: string): Promise<ResponseDto<BestSellerResponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_WEEKLY_BEST_SELLERS, bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 월간 베스트셀러
export const getMonthlyBestSellers = async(accessToken: string): Promise<ResponseDto<BestSellerResponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_MONTHLY_BEST_SELLERS, bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 연간 베스트셀러
export const getYearlyBestSellers = async(accessToken: string): Promise<ResponseDto<BestSellerResponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_YEARLY_BEST_SELLERS, bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 카테고리별 베스트셀러
export const getBestSellersByCategory = async(categoryId: number, accessToken: string): Promise<ResponseDto<BestSellerResponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_BEST_SELLERS_BY_CATEGORY(categoryId), bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 판매량 통계
// 기간별
// 1) daily
export const getDailySalesQuantity = async(month: number, accessToken: string): Promise<ResponseDto<SalesQuantityStatisticsReponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_DAILY_SALES_QUANTITY, {
        ...bearerAuthorization(accessToken),
        params: { month }
      });
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 2) weekly
export const getWeeklySalesQuantity = async(year: number, month: number, accessToken: string): Promise<ResponseDto<SalesQuantityStatisticsReponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_WEEKLY_SALES_QUANTITY, {
        ...bearerAuthorization(accessToken),
        params: { year, month }
      });
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 3) monthly
export const getMonthlySalesQuantity = async(year: number, accessToken: string): Promise<ResponseDto<SalesQuantityStatisticsReponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_MONTHLY_SALES_QUANTITY(year), bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 카테고리별
export const getSalesQuantityByCategory = async(accessToken: string): Promise<ResponseDto<CategorySalesQuantityResponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_SALES_QUANTITY_BY_CATEGORY, bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 지점별
export const getSalesQuantityByBranch = async(year: number, month: number, accessToken: string): Promise<ResponseDto<SalesQuantityStatisticsReponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_SALES_QUANTITY_BY_BRANCH(year, month), bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 할인항목별
export const getSalesQuantityByDiscountPolicy = async(year: number, quarter: number, accessToken: string): Promise<ResponseDto<SalesQuantityStatisticsReponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_SALES_QUANTITY_BY_DISCOUNT_POLICY(year, quarter), bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}
