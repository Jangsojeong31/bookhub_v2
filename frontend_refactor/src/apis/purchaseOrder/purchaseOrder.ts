import { ResponseDto } from "@/dtos";
import { PurchaseOrderCreateRequestDto } from "@/dtos/purchaseOrder/request/purchaseOrder-create.request.dto";
import { PurchaseOrderResponseDto } from "@/dtos/purchaseOrder/response/purchaseOrder.response.dto";
import { axiosInstance, bearerAuthorization, responseErrorHandler, responseSuccessHandler } from "../axiosConfig";
import { DELETE_PURCHASE_ORDER_URL, GET_PURCHASE_ORDER_BY_CRITERIA, POST_PURCHASE_ORDER_URL, PUT_PURCHASE_ORDER_URL } from "../constants/jsj.constants";
import { AxiosError } from "axios";
import { PurchaseOrderRequestDto } from "@/dtos/purchaseOrder/request/purchaseOrder.request.dto";
import { PurchaseOrderStatus } from "@/dtos/purchaseOrderApproval/request/purchaseOrder-approve.request.dto";


// 발주 요청서 작성 (사용자 정보 가져와서 이름, 지점 등 사용)
export const createPurchaseOrder = async(dto: PurchaseOrderCreateRequestDto, accessToken: string): Promise<ResponseDto<PurchaseOrderResponseDto[]>> => {
  try{
    const response = await axiosInstance.post(POST_PURCHASE_ORDER_URL, dto, bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
}

// // 발주 요청서 전체 조회 (날짜순)
// export const getAllPurchaseOrder = async(accessToken: string): Promise<ResponseDto<PurchaseOrderResponseDto[]>> => {
//   try{
//     const response = await axiosInstance.get(GET_ALL_PURCHASE_ORDER_URL, bearerAuthorization(accessToken));
//     return responseSuccessHandler(response);
//   }catch(error){
//     return responseErrorHandler(error as AxiosError<ResponseDto>)
//   }
// }

// 조회 기준으로 조회(발주 담당 사원, isbn, 승인 상태)
export const getAllPurchaseOrderByCriteria = async(employeeName: string, bookIsbn: string, purchaseOrderStatus: PurchaseOrderStatus | null, accessToken: string): Promise<ResponseDto<PurchaseOrderResponseDto[]>> => {
  try{
    const response = await axiosInstance.get(GET_PURCHASE_ORDER_BY_CRITERIA(employeeName, bookIsbn, purchaseOrderStatus), bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 수정(발주량 수정)
export const updatePurchaseOrder = async(purchaseOrderId: number, dto: PurchaseOrderRequestDto, accessToken: string): Promise<ResponseDto<PurchaseOrderResponseDto>> => {
  try{
    const response = await axiosInstance.put(PUT_PURCHASE_ORDER_URL(purchaseOrderId), dto, bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}

// 삭제
export const deletePurchaseOrder = async(purchaseOrderId: number, accessToken: string): Promise<ResponseDto<void>> => {
  try{
    const response = await axiosInstance.delete(DELETE_PURCHASE_ORDER_URL(purchaseOrderId), bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  }catch(error){
    return responseErrorHandler(error as AxiosError<ResponseDto>)
  }
}