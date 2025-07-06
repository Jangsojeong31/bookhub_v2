//# Axios 인스턴스 및 공통 설정

import axios, { AxiosError, AxiosResponse } from "axios";
import { ResponseDto } from "../dtos";

// Axios 인스턴스 생성
export const axiosInstance = axios.create({
  // process.env.REACT_APP~~
  // : REACT 프로젝트에서 환경 변수를 사용하는 방식
  // > 프로젝트 루트에 .env 파일을 생성하여 데이터 저장
  baseURL: import.meta.env.REACT_APP_API_DOMAIN || "http://localhost:8080",
  timeout: 8000,
});

// 공통 응답 처리 (응답 핸들러)
// 1) 성공
export const responseSuccessHandler = <T = any>(response: AxiosResponse<ResponseDto<T>>) => {
  return response.data; // response.data 내부 구조: ResponseDto 구조
}

// 2) 실패
export const responseErrorHandler = (error: AxiosError<ResponseDto>) => {
  if (!error.response) return { code: 'NETWORK_ERROR', message: '네트워크 오류', data: null };
  return error.response.data; // ResponseDto 구조
}

//& function: Authorization Bearer 헤더 //
// >> 인증이 필요한 모든 요청에 JWT access token을 붙이기 위해 사용
export const bearerAuthorization = (accessToken: string) => ({
  headers: { 'Authorization': `Bearer ${accessToken}` }
}); // 객체 단일 반환 불가: 소괄호로 감싸서 전달

//? EX) axios.get(URL, bearerAuthorization(token));