import { ResponseDto } from "@/dtos";
import { EmployeeSignUpApprovalsResponseDto } from "@/dtos/employee/response/employee-sign-up-approvals.response.dto";
import {
  axiosInstance,
  bearerAuthorization,
  responseErrorHandler,
  responseSuccessHandler,
} from "../axiosConfig";
import { GET_ALL_EMPLOYEE_SIGN_UP_APRROVALS_URL } from "../constants/khj.constants";
import { AxiosError } from "axios";

interface SearchParams {
  employeeName?: string;
  authorityName?: string;
  isApproved?: string;
  deniedReason?: string;
  startUpdatedAt?: string;
  endUpdatedAt?: string;
}

export const employeeSignUpApprovalSearchRequest = async (
  params: SearchParams,
  accessToken: string
): Promise<ResponseDto<EmployeeSignUpApprovalsResponseDto[]>> => {
  try {
    const response = await axiosInstance.get(
      GET_ALL_EMPLOYEE_SIGN_UP_APRROVALS_URL,
      { params, ...bearerAuthorization(accessToken) }
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};
