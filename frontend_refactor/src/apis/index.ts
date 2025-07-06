export const tmp = '';

//# ===== API 관련 기능을 관리하는 폴더 ===== //

// 스프링부트(백엔드)의 응답 구조
// : ResponseEntity<ResponseDto<T>> 형식으로 반환

/*
  ? ResponseDto<T> 구조
  public class ResponseDto<T> {
    private String code; // 응답 코드
    private String message; // 응답 메시지
    private T data; // 실제 응답 데이터
  }
*/ 

//& 1. URL 상수 정의: constants.ts 파일 생성
//& 2. Axios 인스턴스 및 공통 설정: axiosConfig.ts
//& 3. 각 기능별 파일에서 요청 / 응답 구조를 정의

//# 모듈 통합 파일 #//
// : 다른 모듈에서 API를 간편하게 한 번에 import 하도록 돕는 용도
// > 무조건! api 사용 시! 
// > import { 원하는 API명, ... , } from '@/apis';

//export * from './auth/auth'; // "./폴더명/파일명"

export * from './constants/csy.constants';
export * from './constants/sjw.constants';
export * from './constants/jsj.constants';
export * from './constants/khj.constants';
