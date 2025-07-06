import { ResponseDto } from "@/dtos";
import { EmployeeExitLogsResponseDto } from "@/dtos/employee/response/employee-exit-logs.response.dto";
import {
  axiosInstance,
  bearerAuthorization,
  responseErrorHandler,
  responseSuccessHandler,
} from "../axiosConfig";
import { GET_ALL_EMPLOYEE_EXIT_LOGS_URL } from "../constants/khj.constants";
import { AxiosError } from "axios";

interface searchParams {
  employeeName?: string;
  authorityName?: string;
  exitReason?: string;
  startUpdatedAt?: string;
  endUpdatedAt?: string;
}

export const employeeExitLogsRequest = async (
  params: searchParams,
  accessToken: string
): Promise<ResponseDto<EmployeeExitLogsResponseDto[]>> => {
  try {
    const response = await axiosInstance.get(GET_ALL_EMPLOYEE_EXIT_LOGS_URL, {
      params,
      ...bearerAuthorization(accessToken),
    });
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};
