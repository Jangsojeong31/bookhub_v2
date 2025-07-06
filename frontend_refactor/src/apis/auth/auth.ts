import { ResponseDto } from "@/dtos";
import { SignUpRequestDto } from "@/dtos/auth/request/sign-up.request.dto";
import {
  axiosInstance,
  responseErrorHandler,
  responseSuccessHandler,
} from "../axiosConfig";
import {
  CHECK_LOGIN_ID_DUPLICATE,
  EMPLOYEE_UPDATE_URL,
  LOGIN_ID_FIND_EMAIL_URL,
  LOGIN_ID_FIND_URL,
  LOGOUT_URL,
  PASSWORD_CHANGE_EMAIL_URL,
  PASSWORD_CHANGE_URL,
  SIGN_IN_URL,
  SIGN_UP_RESULT_URL,
  SIGN_UP_URL,
} from "../constants/khj.constants";
import { AxiosError } from "axios";
import { SignInRequestDto } from "@/dtos/auth/request/sign-in.request.dto";
import { SignInResponseDto } from "@/dtos/auth/response/sign-in.response.dto";
import { LoginIdFindSendEmailRequestDto } from "@/dtos/auth/request/login-id-find-email.request.dto";
import { PasswordChangeEamilRequestDto } from "@/dtos/auth/request/password-change-email.request.dto";
import { PasswordChangeRequestDto } from "@/dtos/auth/request/password-change.request.dto";
import { employeeUpdateRequestDto } from "@/dtos/employee/request/employe-update.request.dto";

export const signUpRequest = async (
  dto: SignUpRequestDto
): Promise<ResponseDto<void>> => {
  try {
    const response = await axiosInstance.post(SIGN_UP_URL, dto);
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const checkLoginIdDuplicate = async (
  loginId: string
): Promise<ResponseDto<void>> => {
  try {
    const response = await axiosInstance.get(
      CHECK_LOGIN_ID_DUPLICATE + `?loginId=${loginId}`
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const signInRequest = async (
  dto: SignInRequestDto
): Promise<ResponseDto<SignInResponseDto>> => {
  try {
    const response = await axiosInstance.post(SIGN_IN_URL, dto);
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const loginIdFindSendEmailRequest = async (
  dto: LoginIdFindSendEmailRequestDto
): Promise<ResponseDto<string>> => {
  try {
    const response = await axiosInstance.post(LOGIN_ID_FIND_EMAIL_URL, dto);
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const loginIdFindRequest = async (
  token: string
): Promise<ResponseDto<string>> => {
  try {
    const response = await axiosInstance.get(
      LOGIN_ID_FIND_URL + `?token=${token}`
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const passwordChangeEmailRequest = async (
  dto: PasswordChangeEamilRequestDto
): Promise<ResponseDto<string>> => {
  try {
    const response = await axiosInstance.post(PASSWORD_CHANGE_EMAIL_URL, dto);
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const verifyToken = async (
  token: string
): Promise<ResponseDto<string>> => {
  try {
    const response = await axiosInstance.get(
      PASSWORD_CHANGE_URL + `?token=${token}`
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const passwordChangeRequest = async (
  token: string,
  dto: PasswordChangeRequestDto
): Promise<ResponseDto<string>> => {
  try {
    const response = await axiosInstance.put(
      PASSWORD_CHANGE_URL + `?token=${token}`,
      dto
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const logoutRequest = async (): Promise<ResponseDto<void>> => {
  try {
    const response = await axiosInstance.post(LOGOUT_URL);
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const signUpResultRequest = async (
  approvalId: number
): Promise<ResponseDto<string>> => {
  try {
    const response = await axiosInstance.post(SIGN_UP_RESULT_URL(approvalId));
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const verifyTokenEmployee = async (
  token: string
): Promise<ResponseDto<string>> => {
  try {
    const response = await axiosInstance.get(
      EMPLOYEE_UPDATE_URL + `?token=${token}`
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const employeeUpdateRequest = async (
  token: string,
  dto: employeeUpdateRequestDto
): Promise<ResponseDto<string>> => {
  try {
    const response = await axiosInstance.put(
      EMPLOYEE_UPDATE_URL + `?token=${token}`,
      dto
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};
