import { ResponseDto } from "@/dtos";
import { PurchaseOrderResponseDto } from "@/dtos/purchaseOrder/response/purchaseOrder.response.dto";
import { AxiosError } from "axios";
import { axiosInstance, bearerAuthorization, responseSuccessHandler, responseErrorHandler } from "../axiosConfig";
import {GET_ALL_PURCHASE_ORDER_REQUESTED_URL, GET_PURCHASE_ORDER_APPROVAL_BY_CRITERIA_URL, GET_PURCHASE_ORDER_APPROVAL_BY_DATE, PUT_PURCHASE_ORDER_STATUS_URL } from "../constants/jsj.constants";
import { PurchaseOrderApproveRequestDto } from "@/dtos/purchaseOrderApproval/request/purchaseOrder-approve.request.dto";
import { PurchaseOrderApprovalResponseDto } from "@/dtos/purchaseOrderApproval/response/purchaseOrderApproval.respose.dto";

// 발주 요청서 업데이트 (요청중인 발주 요청서만 전체 조회)
export const getAllPurchaseOrderRequested = async(accessToken: string): Promise<ResponseDto<PurchaseOrderResponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_ALL_PURCHASE_ORDER_REQUESTED_URL, bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 발주 요청서 승인 / 승인 거절
export const updatePurchaseOrderStatus = async(purchaseOrderId: number, dto: PurchaseOrderApproveRequestDto, accessToken: string): Promise<ResponseDto<PurchaseOrderResponseDto>> => {
  try{
    const response = await axiosInstance.put(PUT_PURCHASE_ORDER_STATUS_URL(purchaseOrderId), dto, bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 승인 기록 전체 조회
// export const getAllPurchaseOrderApproval = async(accessToken: string): Promise<ResponseDto<PurchaseOrderApprovalResponseDto[]>> => {
//   try{
//     const response = await axiosInstance.get(GET_ALL_PURCHASE_ORDER_APPROVAL_URL, bearerAuthorization(accessToken));
//     return responseSuccessHandler(response);
//   }catch(error){
//     return responseErrorHandler(error as AxiosError<ResponseDto>)
//   }
// }

// 조건으로 조회 
export const getAllPurchaseOrderApprovalByCriteria = async( employeeName: string, isApproved: boolean | null, accessToken: string):Promise<ResponseDto<PurchaseOrderApprovalResponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_PURCHASE_ORDER_APPROVAL_BY_CRITERIA_URL(employeeName, isApproved), bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 승인 일자로 조회
export const getAllPurchaseOrderApprovalByDate = async(startDate: string, endDate: string, accessToken: string): Promise<ResponseDto<PurchaseOrderApprovalResponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_PURCHASE_ORDER_APPROVAL_BY_DATE(startDate, endDate), bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}