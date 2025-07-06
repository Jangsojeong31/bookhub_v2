import { ResponseDto } from "@/dtos";
import { EmployeeListResponseDto } from "@/dtos/employee/response/employee-list.response.dto";
import {
  axiosInstance,
  bearerAuthorization,
  responseErrorHandler,
  responseSuccessHandler,
} from "../axiosConfig";
import {
  GET_ALL_EMPLOYEE_URL,
  GET_EMPLOYEE_URL,
  GET_PENDING_EMPLOYEE_URL,
  PUT_EMPLOYEE_APPROVE_URL,
  PUT_EMPLOYEE_CHANGE_URL,
  PUT_EMPLOYEE_STATUS_URL,
} from "../constants/khj.constants";
import { AxiosError } from "axios";
import { EmployeeSignUpApprovalRequestDto } from "@/dtos/employee/request/employee-sign-up-Approval.request.dto";
import { EmployeeDetailResponseDto } from "@/dtos/employee/response/employee-detail.response.dto";
import { EmployeeSignUpListResponseDto } from "@/dtos/employee/response/employee-sign-up-list.response.dto copy";
import { EmployeeChangeRequestDto } from "@/dtos/employee/request/employee-change.request.dto";
import { EmployeeExitUpdateRequestDto } from "@/dtos/employee/request/employee-exit-update.request.dto";

interface SearchEmployeeParams {
  name?: string;
  branchName?: string;
  positionName?: string;
  authorityName?: string;
  status?: string;
}

export const employeeRequest = async (
  params: SearchEmployeeParams,
  accessToken: string
): Promise<ResponseDto<EmployeeListResponseDto[]>> => {
  try {
    const response = await axiosInstance.get(GET_ALL_EMPLOYEE_URL, {
      params,
      ...bearerAuthorization(accessToken),
    });
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const employeeDetailRequest = async (
  employeeId: number,
  accessToken: string
): Promise<ResponseDto<EmployeeDetailResponseDto>> => {
  try {
    const response = await axiosInstance.get(
      GET_EMPLOYEE_URL(employeeId),
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const employeeSignUpListeRequest = async (
  accessToken: string
): Promise<ResponseDto<EmployeeSignUpListResponseDto[]>> => {
  try {
    const response = await axiosInstance.get(
      GET_PENDING_EMPLOYEE_URL,
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const employeeSignUpApprovalRequest = async (
  employeeId: number,
  dto: EmployeeSignUpApprovalRequestDto,
  accessToken: string
): Promise<ResponseDto<void>> => {
  try {
    const response = await axiosInstance.put(
      PUT_EMPLOYEE_APPROVE_URL(employeeId),
      dto,
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const employeeChangeRequestDto = async (
  employeeId: number,
  dto: EmployeeChangeRequestDto,
  accessToken: string
): Promise<ResponseDto<void>> => {
  try {
    const response = await axiosInstance.put(
      PUT_EMPLOYEE_CHANGE_URL(employeeId),
      dto,
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const employeeExitUpdateRequest = async (
  employeeId: number,
  dto: EmployeeExitUpdateRequestDto,
  accessToken: string
): Promise<ResponseDto<void>> => {
  try {
    const response = await axiosInstance.put(
      PUT_EMPLOYEE_STATUS_URL(employeeId),
      dto,
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};
