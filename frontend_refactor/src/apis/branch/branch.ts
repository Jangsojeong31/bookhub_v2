import { ResponseDto } from "@/dtos";
import { BranchSearchResponseDto } from "@/dtos/branch/response/branch-search.respnse.dto";
import {
  axiosInstance,
  bearerAuthorization,
  responseErrorHandler,
  responseSuccessHandler,
} from "../axiosConfig";
import {
  GET_BRANCH_DETAIL_URL,
  GET_BRANCH_URL,
  POST_BRANCH_URL,
  PUT_BRANCH_URL,
} from "../constants/khj.constants";
import { AxiosError } from "axios";
import { BranchCreateRequestDto } from "@/dtos/branch/request/branch-create.request.dto";
import { BranchDetailResponseDto } from "@/dtos/branch/response/branch-detail.response.dto";
import { BranchUpdateRequestDto } from "@/dtos/branch/request/branch-update.request.dto";

interface searchParams {
  branchLocation: string;
}

export const branchSearchRequest = async (
  params: searchParams,
  accessToken: string
): Promise<ResponseDto<BranchSearchResponseDto[]>> => {
  try {
    const response = await axiosInstance.get(GET_BRANCH_URL, {
      params,
      ...bearerAuthorization(accessToken),
    });
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const branchDetailRequest = async (
  branchId: number,
  accessToken: string
): Promise<ResponseDto<BranchDetailResponseDto>> => {
  try {
    const response = await axiosInstance.get(
      GET_BRANCH_DETAIL_URL(branchId),
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const branchCreateRequest = async (
  dto: BranchCreateRequestDto,
  accessToken: string
): Promise<ResponseDto<void>> => {
  try {
    const response = await axiosInstance.post(
      POST_BRANCH_URL,
      dto,
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

export const branchUpdateRequest = async (
  branchId: number,
  dto: BranchUpdateRequestDto,
  accessToken: string
): Promise<ResponseDto<void>> => {
  try {
    const response = await axiosInstance.put(
      PUT_BRANCH_URL(branchId),
      dto,
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};