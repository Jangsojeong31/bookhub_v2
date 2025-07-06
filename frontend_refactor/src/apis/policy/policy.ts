import {
  axiosInstance,
  responseSuccessHandler,
  responseErrorHandler,
  bearerAuthorization
} from '@/apis/axiosConfig';
import axios, { AxiosError, AxiosResponse } from 'axios';
import { ResponseDto } from '@/dtos';
import { PageResponseDto } from '@/dtos/page-response.dto';
import { PolicyDetailResponseDto, PolicyListResponseDto } from '@/dtos/policy/policy.response.dto';
import {
  PolicyCreateRequestDto,
  PolicyUpdateRequestDto
} from '@/dtos/policy/policy.request.dto';
import { PolicyType } from '@/apis/enums/PolicyType';
import { DELETE_POLICY_URL, GET_ALL_POLICIES_URL, GET_POLICY_URL, POST_POLICY_URL, PUT_POLICY_URL } from '../constants/csy.constants';

/**
 * 정책 목록 조회 (페이징 + 검색)
 */
export const getPolicies = async (
  accessToken: string,
  page: number,
  size: number,
  keyword?: string,
  type?: PolicyType,
  start?: string,
  end?: string
): Promise<ResponseDto<PageResponseDto<PolicyListResponseDto>>> => {
  try {
    const response: AxiosResponse<
      ResponseDto<PageResponseDto<PolicyListResponseDto>>
    > = await axios.get(GET_ALL_POLICIES_URL, {
      params: { page, size, keyword, type, start, end },
      headers: { Authorization: `Bearer ${accessToken}` },
    });
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(
      error as AxiosError<ResponseDto<PageResponseDto<PolicyListResponseDto>>>
    );
  }
};

/**
 * 정책 생성 (Admin 전용)
 */
export const createPolicy = async (
  dto: PolicyCreateRequestDto,
  accessToken: string
): Promise<ResponseDto<null>> => {
  try {
    const response = await axiosInstance.post(
      POST_POLICY_URL,
      dto,
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto<null>>);
  }
};

/**
 * 정책 수정 (Admin 전용)
 */
export const updatePolicy = async (
  policyId: number,
  dto: PolicyUpdateRequestDto,
  accessToken: string
): Promise<ResponseDto<null>> => {
  try {
    const response = await axiosInstance.put(
      PUT_POLICY_URL(policyId),
      dto,
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto<null>>);
  }
};

/**
 * 정책 삭제 (Admin 전용)
 */
export const deletePolicy = async (
  policyId: number,
  accessToken: string
): Promise<ResponseDto<null>> => {
  try {
    const response = await axiosInstance.delete(
      DELETE_POLICY_URL(policyId),
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto<null>>);
  }
};

export const getPolicyDetail = async (
  policyId: number,
  accessToken: string
): Promise<ResponseDto<PolicyDetailResponseDto>> => {
  try {
    const res = await axiosInstance.get(
      GET_POLICY_URL(policyId),
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(res);
  } catch (err:any) {
    return responseErrorHandler(err);
  }
};
