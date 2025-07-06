import { ResponseDto } from "@/dtos";
import { axiosInstance, bearerAuthorization, responseErrorHandler, responseSuccessHandler } from "../axiosConfig";
import { GET_ALERT_URL, GET_UNREAD_ALERT_URL, PUT_ALERT_URL } from "../constants/sjw.constants";
import axios, { AxiosError } from "axios";
import { AlertResponseDto } from "@/dtos/alert/response/alert.response.dto";

// 미확인 알림 목록 조회
export const getUnreadAlerts = async (
  employeeId: number,
  accessToken: string
): Promise<ResponseDto<any[]>> => {
  try {
    const url = GET_UNREAD_ALERT_URL.replace("{employeeId}", String(employeeId));
    const response = await axiosInstance.get(url, bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

// 모든 알림 조회
export const getAllAlerts = async (
  employeeId: number,
  accessToken: string
): Promise<ResponseDto<AlertResponseDto[]>> => {
  try {
    const url = GET_ALERT_URL.replace("{employeeId}", String(employeeId));
    const response = await axiosInstance.get(url, bearerAuthorization(accessToken));
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const markAlertsAsRead = async (
  alertIds: number[],
  accessToken: string
): Promise<ResponseDto<null>> => {
  try {
    const response = await axiosInstance.put(
      PUT_ALERT_URL,
      { alertIds },
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export async function getUnreadAlertCount(employeeId: number, token: string): Promise<ResponseDto<number>> {
  const res = await axios.get(`/api/alerts/unread-count?employeeId=${employeeId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return res.data;
}

export function getAlertTargetUrl(alert: AlertResponseDto): string | null {
  const { alertType } = alert;

  switch (alertType) {

    case "PURCHASE_REQUESTED":
      return `/purchase-order/approve`;

    case "PURCHASE_APPROVED":
      return `/purchase-order/else`;

    case "BOOK_RECEIVED_SUCCESS":
      return `/reception/logs`;

    case "NOTICE":
      return `/policies`;

    case "SIGNUP_APPROVAL":
      return `/employees/approval`;

    case "STOCK_LOW":
    case "STOCK_OUT":
      return `/stocks`;

    default:
      return null;
  }
}
