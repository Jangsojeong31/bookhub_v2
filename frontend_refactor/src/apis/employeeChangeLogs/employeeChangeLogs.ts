import { ResponseDto } from "@/dtos";
import { EmployeeChangeLogsResponseDto } from "@/dtos/employee/response/employee-change-logs.response.dto";
import {
  axiosInstance,
  bearerAuthorization,
  responseErrorHandler,
  responseSuccessHandler,
} from "../axiosConfig";
import { GET_ALL_EMPLOYEE_CHANGE_LOGS_URL } from "../constants/khj.constants";
import { AxiosError } from "axios";

interface searchParams {
  employeeName?: string;
  authorityName?: string;
  changeType?: string;
  startUpdatedAt?: string;
  endUpdatedAt?: string;
}

export const employeeChangeLogsSearchRequest = async (
  params: searchParams,
  token: string
): Promise<ResponseDto<EmployeeChangeLogsResponseDto[]>> => {
  try {
    const response = await axiosInstance.get(GET_ALL_EMPLOYEE_CHANGE_LOGS_URL, {
      params,
      ...bearerAuthorization(token),
    });
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};
